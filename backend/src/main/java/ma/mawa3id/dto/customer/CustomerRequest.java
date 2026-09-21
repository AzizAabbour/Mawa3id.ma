package ma.mawa3id.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.validation.MoroccanPhone;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    @NotBlank(message = "Le nom complet est obligatoire")
    private String fullName;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @MoroccanPhone
    private String phone;

    @MoroccanPhone
    private String whatsappNumber;

    @Email(message = "Format d'email invalide")
    private String email;

    private String notes;
}
