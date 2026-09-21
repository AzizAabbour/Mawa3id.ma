package ma.mawa3id.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import ma.mawa3id.domain.entity.Appointment;
import ma.mawa3id.domain.entity.Customer;
import ma.mawa3id.domain.entity.Employee;
import ma.mawa3id.domain.entity.Service;
import ma.mawa3id.dto.appointment.AppointmentResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-21T16:14:26+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 21.0.12.1 (Eclipse Adoptium)"
)
@Component
public class AppointmentMapperImpl implements AppointmentMapper {

    @Override
    public AppointmentResponse toResponse(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }

        AppointmentResponse.AppointmentResponseBuilder appointmentResponse = AppointmentResponse.builder();

        appointmentResponse.customerId( appointmentCustomerId( appointment ) );
        appointmentResponse.customerPublicId( appointmentCustomerPublicId( appointment ) );
        appointmentResponse.customerName( appointmentCustomerFullName( appointment ) );
        appointmentResponse.customerPhone( appointmentCustomerPhone( appointment ) );
        appointmentResponse.customerWhatsapp( appointmentCustomerWhatsappNumber( appointment ) );
        appointmentResponse.customerEmail( appointmentCustomerEmail( appointment ) );
        appointmentResponse.serviceId( appointmentServiceId( appointment ) );
        appointmentResponse.servicePublicId( appointmentServicePublicId( appointment ) );
        appointmentResponse.serviceName( appointmentServiceName( appointment ) );
        appointmentResponse.serviceDurationMinutes( appointmentServiceDurationMinutes( appointment ) );
        appointmentResponse.servicePriceMad( appointmentServicePriceMad( appointment ) );
        appointmentResponse.serviceColor( appointmentServiceColor( appointment ) );
        appointmentResponse.employeeId( appointmentEmployeeId( appointment ) );
        appointmentResponse.employeePublicId( appointmentEmployeePublicId( appointment ) );
        appointmentResponse.appointmentDate( appointment.getAppointmentDate() );
        appointmentResponse.businessId( appointment.getBusinessId() );
        appointmentResponse.cancellationReason( appointment.getCancellationReason() );
        appointmentResponse.cancelledAt( appointment.getCancelledAt() );
        appointmentResponse.completedAt( appointment.getCompletedAt() );
        appointmentResponse.createdAt( appointment.getCreatedAt() );
        appointmentResponse.endTime( appointment.getEndTime() );
        appointmentResponse.id( appointment.getId() );
        appointmentResponse.notes( appointment.getNotes() );
        appointmentResponse.publicId( appointment.getPublicId() );
        appointmentResponse.startTime( appointment.getStartTime() );
        appointmentResponse.status( appointment.getStatus() );

        return appointmentResponse.build();
    }

    @Override
    public List<AppointmentResponse> toResponseList(List<Appointment> appointments) {
        if ( appointments == null ) {
            return null;
        }

        List<AppointmentResponse> list = new ArrayList<AppointmentResponse>( appointments.size() );
        for ( Appointment appointment : appointments ) {
            list.add( toResponse( appointment ) );
        }

        return list;
    }

    private Long appointmentCustomerId(Appointment appointment) {
        Customer customer = appointment.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getId();
    }

    private UUID appointmentCustomerPublicId(Appointment appointment) {
        Customer customer = appointment.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getPublicId();
    }

    private String appointmentCustomerFullName(Appointment appointment) {
        Customer customer = appointment.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getFullName();
    }

    private String appointmentCustomerPhone(Appointment appointment) {
        Customer customer = appointment.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getPhone();
    }

    private String appointmentCustomerWhatsappNumber(Appointment appointment) {
        Customer customer = appointment.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getWhatsappNumber();
    }

    private String appointmentCustomerEmail(Appointment appointment) {
        Customer customer = appointment.getCustomer();
        if ( customer == null ) {
            return null;
        }
        return customer.getEmail();
    }

    private Long appointmentServiceId(Appointment appointment) {
        Service service = appointment.getService();
        if ( service == null ) {
            return null;
        }
        return service.getId();
    }

    private UUID appointmentServicePublicId(Appointment appointment) {
        Service service = appointment.getService();
        if ( service == null ) {
            return null;
        }
        return service.getPublicId();
    }

    private String appointmentServiceName(Appointment appointment) {
        Service service = appointment.getService();
        if ( service == null ) {
            return null;
        }
        return service.getName();
    }

    private Integer appointmentServiceDurationMinutes(Appointment appointment) {
        Service service = appointment.getService();
        if ( service == null ) {
            return null;
        }
        return service.getDurationMinutes();
    }

    private BigDecimal appointmentServicePriceMad(Appointment appointment) {
        Service service = appointment.getService();
        if ( service == null ) {
            return null;
        }
        return service.getPriceMad();
    }

    private String appointmentServiceColor(Appointment appointment) {
        Service service = appointment.getService();
        if ( service == null ) {
            return null;
        }
        return service.getColor();
    }

    private Long appointmentEmployeeId(Appointment appointment) {
        Employee employee = appointment.getEmployee();
        if ( employee == null ) {
            return null;
        }
        return employee.getId();
    }

    private UUID appointmentEmployeePublicId(Appointment appointment) {
        Employee employee = appointment.getEmployee();
        if ( employee == null ) {
            return null;
        }
        return employee.getPublicId();
    }
}
