package ma.mawa3id.mapper;

import ma.mawa3id.domain.entity.Appointment;
import ma.mawa3id.dto.appointment.AppointmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppointmentMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerPublicId", source = "customer.publicId")
    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "customerPhone", source = "customer.phone")
    @Mapping(target = "customerWhatsapp", source = "customer.whatsappNumber")
    @Mapping(target = "customerEmail", source = "customer.email")
    @Mapping(target = "serviceId", source = "service.id")
    @Mapping(target = "servicePublicId", source = "service.publicId")
    @Mapping(target = "serviceName", source = "service.name")
    @Mapping(target = "serviceDurationMinutes", source = "service.durationMinutes")
    @Mapping(target = "servicePriceMad", source = "service.priceMad")
    @Mapping(target = "serviceColor", source = "service.color")
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeePublicId", source = "employee.publicId")
    AppointmentResponse toResponse(Appointment appointment);

    List<AppointmentResponse> toResponseList(List<Appointment> appointments);
}
