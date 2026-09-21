package ma.mawa3id.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.mawa3id.domain.entity.Appointment;
import ma.mawa3id.domain.entity.Customer;
import ma.mawa3id.domain.entity.Employee;
import ma.mawa3id.domain.entity.Service;
import ma.mawa3id.domain.enums.AppointmentStatus;
import ma.mawa3id.domain.enums.NotificationType;
import ma.mawa3id.dto.appointment.AppointmentRequest;
import ma.mawa3id.dto.appointment.AppointmentResponse;
import ma.mawa3id.dto.appointment.AppointmentStatusUpdateRequest;
import ma.mawa3id.exception.DoubleBookingException;
import ma.mawa3id.exception.ResourceNotFoundException;
import ma.mawa3id.mapper.AppointmentMapper;
import ma.mawa3id.repository.AppointmentRepository;
import ma.mawa3id.repository.CustomerRepository;
import ma.mawa3id.repository.EmployeeRepository;
import ma.mawa3id.repository.ServiceRepository;
import ma.mawa3id.tenant.TenantContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final ServiceRepository serviceRepository;
    private final EmployeeRepository employeeRepository;
    private final AppointmentMapper appointmentMapper;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getAppointments(
            AppointmentStatus status, Long employeeId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Long businessId = TenantContext.getCurrentBusinessId();
        return appointmentRepository.findFiltered(businessId, status, employeeId, startDate, endDate, pageable)
                .map(appointmentMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsForCalendar(LocalDate startDate, LocalDate endDate) {
        Long businessId = TenantContext.getCurrentBusinessId();
        List<Appointment> appointments = appointmentRepository
                .findByBusinessIdAndAppointmentDateBetween(businessId, startDate, endDate);
        return appointmentMapper.toResponseList(appointments);
    }

    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long id) {
        Long businessId = TenantContext.getCurrentBusinessId();
        Appointment appointment = appointmentRepository.findById(id)
                .filter(a -> a.getBusinessId().equals(businessId))
                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous", "id", id));
        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        Long businessId = TenantContext.getCurrentBusinessId();

        Customer customer = customerRepository.findById(request.getCustomerId())
                .filter(c -> c.getBusinessId().equals(businessId))
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));

        Service service = serviceRepository.findById(request.getServiceId())
                .filter(s -> s.getBusinessId().equals(businessId))
                .orElseThrow(() -> new ResourceNotFoundException("Prestation introuvable"));

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .filter(e -> e.getBusinessId().equals(businessId))
                .orElseThrow(() -> new ResourceNotFoundException("Collaborateur introuvable"));

        LocalTime startTime = request.getStartTime();
        LocalTime endTime = startTime.plusMinutes(service.getDurationMinutes());

        // PREVENT DOUBLE BOOKING
        boolean hasOverlap = appointmentRepository.existsOverlapping(
                employee.getId(), request.getAppointmentDate(), startTime, endTime, null);
        if (hasOverlap) {
            throw new DoubleBookingException(
                    "Ce créneau horaire est déjà occupé pour " + employee.getTitle() + " " + request.getAppointmentDate());
        }

        Appointment appointment = Appointment.builder()
                .businessId(businessId)
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

        // Dispatch WhatsApp & SMS notification
        notificationService.sendAppointmentNotification(appointment, NotificationType.BOOKING_CONFIRMATION);

        return appointmentMapper.toResponse(appointment);
    }

    @Transactional
    public AppointmentResponse updateStatus(Long id, AppointmentStatusUpdateRequest request) {
        Long businessId = TenantContext.getCurrentBusinessId();
        Appointment appointment = appointmentRepository.findById(id)
                .filter(a -> a.getBusinessId().equals(businessId))
                .orElseThrow(() -> new ResourceNotFoundException("Rendez-vous introuvable"));

        AppointmentStatus oldStatus = appointment.getStatus();
        appointment.setStatus(request.getStatus());

        if (request.getStatus() == AppointmentStatus.CANCELLED) {
            appointment.setCancelledAt(LocalDateTime.now());
            appointment.setCancellationReason(request.getCancellationReason());
            notificationService.sendAppointmentNotification(appointment, NotificationType.CANCELLATION);
        } else if (request.getStatus() == AppointmentStatus.COMPLETED) {
            appointment.setCompletedAt(LocalDateTime.now());
        }

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }
}
