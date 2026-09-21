package ma.mawa3id.dto.business;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.domain.enums.BusinessCategory;
import ma.mawa3id.validation.MoroccanPhone;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessUpdateRequest {
    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    private String name;
    private String description;
    @MoroccanPhone
    private String phone;
    @MoroccanPhone
    private String whatsappNumber;
    private String email;
    private String address;
    private String city;
    private String region;
    private String googleMapsUrl;
    private BusinessCategory category;
    private String logoUrl;
    private String coverImageUrl;
}
