package ma.mawa3id.mapper;

import javax.annotation.processing.Generated;
import ma.mawa3id.domain.entity.Business;
import ma.mawa3id.dto.business.BusinessPublicResponse;
import ma.mawa3id.dto.business.BusinessResponse;
import ma.mawa3id.dto.business.BusinessUpdateRequest;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-21T16:14:26+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 21.0.12.1 (Eclipse Adoptium)"
)
@Component
public class BusinessMapperImpl implements BusinessMapper {

    @Override
    public BusinessResponse toResponse(Business business) {
        if ( business == null ) {
            return null;
        }

        BusinessResponse.BusinessResponseBuilder businessResponse = BusinessResponse.builder();

        businessResponse.active( business.isActive() );
        businessResponse.address( business.getAddress() );
        businessResponse.category( business.getCategory() );
        businessResponse.city( business.getCity() );
        businessResponse.coverImageUrl( business.getCoverImageUrl() );
        businessResponse.createdAt( business.getCreatedAt() );
        businessResponse.description( business.getDescription() );
        businessResponse.email( business.getEmail() );
        businessResponse.googleMapsUrl( business.getGoogleMapsUrl() );
        businessResponse.id( business.getId() );
        businessResponse.logoUrl( business.getLogoUrl() );
        businessResponse.name( business.getName() );
        businessResponse.phone( business.getPhone() );
        businessResponse.publicId( business.getPublicId() );
        businessResponse.region( business.getRegion() );
        businessResponse.slug( business.getSlug() );
        businessResponse.timezone( business.getTimezone() );
        businessResponse.whatsappNumber( business.getWhatsappNumber() );

        return businessResponse.build();
    }

    @Override
    public BusinessPublicResponse toPublicResponse(Business business) {
        if ( business == null ) {
            return null;
        }

        BusinessPublicResponse.BusinessPublicResponseBuilder businessPublicResponse = BusinessPublicResponse.builder();

        businessPublicResponse.address( business.getAddress() );
        businessPublicResponse.category( business.getCategory() );
        businessPublicResponse.city( business.getCity() );
        businessPublicResponse.coverImageUrl( business.getCoverImageUrl() );
        businessPublicResponse.description( business.getDescription() );
        businessPublicResponse.googleMapsUrl( business.getGoogleMapsUrl() );
        businessPublicResponse.logoUrl( business.getLogoUrl() );
        businessPublicResponse.name( business.getName() );
        businessPublicResponse.phone( business.getPhone() );
        businessPublicResponse.publicId( business.getPublicId() );
        businessPublicResponse.slug( business.getSlug() );
        businessPublicResponse.whatsappNumber( business.getWhatsappNumber() );

        return businessPublicResponse.build();
    }

    @Override
    public void updateEntityFromDto(BusinessUpdateRequest dto, Business business) {
        if ( dto == null ) {
            return;
        }

        business.setAddress( dto.getAddress() );
        business.setCategory( dto.getCategory() );
        business.setCity( dto.getCity() );
        business.setCoverImageUrl( dto.getCoverImageUrl() );
        business.setDescription( dto.getDescription() );
        business.setEmail( dto.getEmail() );
        business.setGoogleMapsUrl( dto.getGoogleMapsUrl() );
        business.setLogoUrl( dto.getLogoUrl() );
        business.setName( dto.getName() );
        business.setPhone( dto.getPhone() );
        business.setRegion( dto.getRegion() );
        business.setWhatsappNumber( dto.getWhatsappNumber() );
    }
}
