package ma.mawa3id.service;

import ma.mawa3id.domain.entity.Customer;
import ma.mawa3id.domain.entity.Employee;
import ma.mawa3id.domain.entity.Service;
import ma.mawa3id.domain.enums.AppointmentStatus;
import ma.mawa3id.dto.appointment.AppointmentRequest;
import ma.mawa3id.dto.appointment.AppointmentResponse;
import ma.mawa3id.exception.DoubleBookingException;
import ma.mawa3id.mapper.AppointmentMapper;
import ma.mawa3id.repository.AppointmentRepository;
import ma.mawa3id.repository.CustomerRepository;
import ma.mawa3id.repository.EmployeeRepository;
import ma.mawa3id.repository.ServiceRepository;
import ma.mawa3id.tenant.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AppointmentService appointmentService;

    private final Long businessId = 1L;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentBusinessId(businessId);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testCreateAppointment_Success() {
        Customer customer = Customer.builder().businessId(businessId).fullName("Anas Alami").totalAppointments(0).totalSpentMad(BigDecimal.ZERO).build();
        customer.setId(10L);

        Service service = Service.builder().businessId(businessId).name("Coupe Cheveux").durationMinutes(30).priceMad(BigDecimal.valueOf(80)).build();
        service.setId(20L);

        Employee employee = Employee.builder().businessId(businessId).title("Coiffeur Pro").build();
        employee.setId(30L);

        AppointmentRequest request = AppointmentRequest.builder()
                .customerId(10L)
                .serviceId(20L)
                .employeeId(30L)
                .appointmentDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(14, 0))
                .build();

        when(customerRepository.findById(10L)).thenReturn(Optional.of(customer));
        when(serviceRepository.findById(20L)).thenReturn(Optional.of(service));
        when(employeeRepository.findById(30L)).thenReturn(Optional.of(employee));
        when(appointmentRepository.existsOverlapping(eq(30L), any(), any(), any(), isNull())).thenReturn(false);
        when(appointmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(appointmentMapper.toResponse(any())).thenReturn(AppointmentResponse.builder().id(100L).status(AppointmentStatus.CONFIRMED).build());

        AppointmentResponse response = appointmentService.createAppointment(request);

        assertNotNull(response);
        assertEquals(AppointmentStatus.CONFIRMED, response.getStatus());
        verify(notificationService).sendAppointmentNotification(any(), any());
    }

    @Test
    void testCreateAppointment_DoubleBookingPrevention() {
        Customer customer = Customer.builder().businessId(businessId).fullName("Anas Alami").build();
        Service service = Service.builder().businessId(businessId).durationMinutes(30).priceMad(BigDecimal.valueOf(80)).build();
        Employee employee = Employee.builder().businessId(businessId).title("Coiffeur Pro").build();
        employee.setId(30L);

        AppointmentRequest request = AppointmentRequest.builder()
                .customerId(10L)
                .serviceId(20L)
                .employeeId(30L)
                .appointmentDate(LocalDate.now().plusDays(1))
                .startTime(LocalTime.of(14, 0))
                .build();

        when(customerRepository.findById(10L)).thenReturn(Optional.of(customer));
        when(serviceRepository.findById(20L)).thenReturn(Optional.of(service));
        when(employeeRepository.findById(30L)).thenReturn(Optional.of(employee));
        // Overlap detected!
        when(appointmentRepository.existsOverlapping(eq(30L), any(), any(), any(), isNull())).thenReturn(true);

        assertThrows(DoubleBookingException.class, () -> appointmentService.createAppointment(request));
        verify(appointmentRepository, never()).save(any());
    }
}
