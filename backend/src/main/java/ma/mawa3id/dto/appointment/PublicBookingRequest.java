package ma.mawa3id.dto.appointment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.validation.MoroccanPhone;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicBookingRequest {

    @NotNull(message = "Le service est obligatoire")
    private Long serviceId;

    private Long employeeId; // Optional: system assigns if null

    @NotNull(message = "La date est obligatoire")
    private LocalDate appointmentDate;

    @NotNull(message = "L'heure est obligatoire")
    private LocalTime startTime;

    @NotBlank(message = "Votre nom complet est obligatoire")
    private String customerName;

    @NotBlank(message = "Votre numéro de téléphone est obligatoire")
    @MoroccanPhone
    private String customerPhone;

    @Email(message = "Format d'email invalide")
    private String customerEmail;

    private String notes;
}
