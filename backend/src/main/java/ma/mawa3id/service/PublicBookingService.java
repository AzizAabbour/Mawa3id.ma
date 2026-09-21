package ma.mawa3id.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.mawa3id.domain.entity.*;
import ma.mawa3id.domain.enums.AppointmentStatus;
import ma.mawa3id.domain.enums.NotificationType;
import ma.mawa3id.dto.appointment.AppointmentResponse;
import ma.mawa3id.dto.appointment.AvailableSlotDto;
import ma.mawa3id.dto.appointment.PublicBookingRequest;
import ma.mawa3id.dto.business.BusinessPublicResponse;
import ma.mawa3id.dto.service.ServiceResponse;
import ma.mawa3id.exception.DoubleBookingException;
import ma.mawa3id.exception.ResourceNotFoundException;
import ma.mawa3id.mapper.AppointmentMapper;
import ma.mawa3id.mapper.BusinessMapper;
import ma.mawa3id.mapper.ServiceMapper;
import ma.mawa3id.repository.*;
import ma.mawa3id.validation.MoroccanPhoneValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service powering public friction-free client booking (/booking/{businessSlug}).
 * No account needed for end-clients.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PublicBookingService {

    private final BusinessRepository businessRepository;
    private final ServiceRepository serviceRepository;
    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;
    private final AppointmentRepository appointmentRepository;
    private final WorkingHourRepository workingHourRepository;
    private final BusinessMapper businessMapper;
    private final ServiceMapper serviceMapper;
    private final AppointmentMapper appointmentMapper;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public BusinessPublicResponse getBusinessBySlug(String slug) {
        Business business = businessRepository.findBySlug(slug)
                .filter(Business::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Établissement introuvable ou inactif"));

        List<ma.mawa3id.domain.entity.Service> services = serviceRepository.findByBusinessIdAndActiveTrue(business.getId());
        List<ServiceResponse> serviceResponses = serviceMapper.toResponseList(services);

        BusinessPublicResponse response = businessMapper.toPublicResponse(business);
        response.setServices(serviceResponses);
        return response;
    }

    @Transactional(readOnly = true)
    public List<AvailableSlotDto> getAvailableSlots(String slug, Long serviceId, Long employeeId, LocalDate date) {
        Business business = businessRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Établissement introuvable"));

        ma.mawa3id.domain.entity.Service service = serviceRepository.findById(serviceId)
                .filter(s -> s.getBusinessId().equals(business.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Prestation introuvable"));

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        WorkingHour workingHour = workingHourRepository
                .findByBusinessIdAndEmployeeIdIsNullAndDayOfWeek(business.getId(), dayOfWeek)
                .orElse(WorkingHour.builder()
                        .startTime(LocalTime.of(9, 0))
                        .endTime(LocalTime.of(20, 0))
                        .dayOff(dayOfWeek == DayOfWeek.SUNDAY)
                        .build());

        if (workingHour.isDayOff()) {
            return List.of();
        }

        // Get employees who can perform this service
        List<Employee> eligibleEmployees;
        if (employeeId != null) {
            eligibleEmployees = employeeRepository.findById(employeeId)
                    .filter(Employee::isActive)
                    .map(List::of)
                    .orElse(List.of());
        } else {
            eligibleEmployees = service.getAssignedEmployees().isEmpty() ?
                    employeeRepository.findByBusinessIdAndActiveTrue(business.getId()) :
                    service.getAssignedEmployees().stream().filter(Employee::isActive).toList();
        }

        if (eligibleEmployees.isEmpty()) {
            return List.of();
        }

        List<AvailableSlotDto> slots = new ArrayList<>();
        LocalTime current = workingHour.getStartTime();
        int stepMinutes = 30; // 30-min booking intervals

        while (current.plusMinutes(service.getDurationMinutes()).isBefore(workingHour.getEndTime()) ||
                current.plusMinutes(service.getDurationMinutes()).equals(workingHour.getEndTime())) {

            LocalTime slotStart = current;
            LocalTime slotEnd = current.plusMinutes(service.getDurationMinutes());

            // Skip break time
            boolean isBreak = workingHour.getBreakStartTime() != null && workingHour.getBreakEndTime() != null &&
                    (slotStart.isBefore(workingHour.getBreakEndTime()) && slotEnd.isAfter(workingHour.getBreakStartTime()));

            if (!isBreak) {
                // Check if any eligible employee is free
                for (Employee emp : eligibleEmployees) {
                    boolean occupied = appointmentRepository.existsOverlapping(
                            emp.getId(), date, slotStart, slotEnd, null);
                    if (!occupied) {
                        slots.add(AvailableSlotDto.builder()
                                .startTime(slotStart)
                                .endTime(slotEnd)
                                .available(true)
                                .employeeId(emp.getId())
                                .build());
                        break; // Slot is available with at least one employee
                    }
                }
            }

            current = current.plusMinutes(stepMinutes);
        }

        return slots;
    }

    @Transactional
    public AppointmentResponse bookPublicAppointment(String slug, PublicBookingRequest request) {
        Business business = businessRepository.findBySlug(slug)
                .filter(Business::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Établissement introuvable"));

        ma.mawa3id.domain.entity.Service service = serviceRepository.findById(request.getServiceId())
                .filter(s -> s.getBusinessId().equals(business.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Prestation introuvable"));

        // Select employee
        Employee employee;
        if (request.getEmployeeId() != null) {
            employee = employeeRepository.findById(request.getEmployeeId())
                    .filter(e -> e.getBusinessId().equals(business.getId()) && e.isActive())
                    .orElseThrow(() -> new ResourceNotFoundException("Collaborateur introuvable"));
        } else {
            List<Employee> available = employeeRepository.findByBusinessIdAndActiveTrue(business.getId());
            if (available.isEmpty()) {
                throw new ResourceNotFoundException("Aucun collaborateur disponible");
            }
            employee = available.get(0);
        }

        LocalTime startTime = request.getStartTime();
        LocalTime endTime = startTime.plusMinutes(service.getDurationMinutes());

        // Check collision
        if (appointmentRepository.existsOverlapping(employee.getId(), request.getAppointmentDate(), startTime, endTime, null)) {
            throw new DoubleBookingException("Ce créneau n'est plus disponible. Veuillez en sélectionner un autre.");
        }

        // Find or create customer
        String normalizedPhone = MoroccanPhoneValidator.normalize(request.getCustomerPhone());
        Customer customer = customerRepository.findByBusinessIdAndPhone(business.getId(), normalizedPhone)
                .orElseGet(() -> {
                    Customer newCustomer = Customer.builder()
                            .businessId(business.getId())
                            .fullName(request.getCustomerName())
                            .phone(normalizedPhone)
                            .whatsappNumber(normalizedPhone)
                            .email(request.getCustomerEmail())
                            .build();
                    return customerRepository.save(newCustomer);
                });

        // Create appointment
        Appointment appointment = Appointment.builder()
                .businessId(business.getId())
                .customer(customer)
                .service(service)
                .employee(employee)
                .appointmentDate(request.getAppointmentDate())
                .startTime(startTime)
                .endTime(endTime)
                .status(AppointmentStatus.CONFIRMED)
                .notes(request.getNotes())
                .build();

        appointment = appointmentRepository.save(appointment);

        // Update customer statistics
        customer.setTotalAppointments(customer.getTotalAppointments() + 1);
        customer.setTotalSpentMad(customer.getTotalSpentMad().add(service.getPriceMad()));
        customer.setLastAppointmentAt(LocalDateTime.of(appointment.getAppointmentDate(), appointment.getStartTime()));
        customerRepository.save(customer);

        // Send confirmation via WhatsApp & SMS
        notificationService.sendAppointmentNotification(appointment, NotificationType.BOOKING_CONFIRMATION);

        return appointmentMapper.toResponse(appointment);
    }
}
