package ma.mawa3id.mapper;

import ma.mawa3id.domain.entity.Service;
import ma.mawa3id.dto.service.ServiceRequest;
import ma.mawa3id.dto.service.ServiceResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceMapper {

    @Mapping(target = "assignedEmployees", source = "assignedEmployees")
    ServiceResponse toResponse(Service service);

    List<ServiceResponse> toResponseList(List<Service> services);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "assignedEmployees", ignore = true)
    Service toEntity(ServiceRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "assignedEmployees", ignore = true)
    void updateEntityFromDto(ServiceRequest request, @MappingTarget Service service);
}
