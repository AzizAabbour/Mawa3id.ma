package ma.mawa3id.dto.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.domain.enums.BusinessCategory;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessResponse {
    private Long id;
    private UUID publicId;
    private String name;
    private String slug;
    private String logoUrl;
    private String coverImageUrl;
    private String description;
    private String phone;
    private String whatsappNumber;
    private String email;
    private String address;
    private String city;
    private String region;
    private String googleMapsUrl;
    private BusinessCategory category;
    private String timezone;
    private boolean active;
    private LocalDateTime createdAt;
}
