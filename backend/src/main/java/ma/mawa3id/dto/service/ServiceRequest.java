package ma.mawa3id.dto.service;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {

    @NotBlank(message = "Le nom du service est obligatoire")
    private String name;

    private String description;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 5, message = "La durée minimale est de 5 minutes")
    private Integer durationMinutes;

    @NotNull(message = "Le prix est obligatoire")
    @Min(value = 0, message = "Le prix ne peut pas être négatif")
    private BigDecimal priceMad;

    private String color;

    private boolean active = true;

    private Set<Long> employeeIds;
}
