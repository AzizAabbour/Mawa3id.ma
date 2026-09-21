package ma.mawa3id.mapper;

import ma.mawa3id.domain.entity.Customer;
import ma.mawa3id.dto.customer.CustomerRequest;
import ma.mawa3id.dto.customer.CustomerResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {
    CustomerResponse toResponse(Customer customer);
    List<CustomerResponse> toResponseList(List<Customer> customers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "totalAppointments", ignore = true)
    @Mapping(target = "totalSpentMad", ignore = true)
    Customer toEntity(CustomerRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    void updateEntityFromDto(CustomerRequest request, @MappingTarget Customer customer);
}
