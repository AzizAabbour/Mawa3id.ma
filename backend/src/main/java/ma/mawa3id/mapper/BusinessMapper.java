package ma.mawa3id.mapper;

import ma.mawa3id.domain.entity.Business;
import ma.mawa3id.dto.business.BusinessPublicResponse;
import ma.mawa3id.dto.business.BusinessResponse;
import ma.mawa3id.dto.business.BusinessUpdateRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BusinessMapper {
    BusinessResponse toResponse(Business business);
    BusinessPublicResponse toPublicResponse(Business business);
    void updateEntityFromDto(BusinessUpdateRequest dto, @MappingTarget Business business);
}
