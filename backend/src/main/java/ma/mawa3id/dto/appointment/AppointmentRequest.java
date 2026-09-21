package ma.mawa3id.dto.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    @NotNull(message = "Le client est obligatoire")
    private Long customerId;

    @NotNull(message = "Le service est obligatoire")
    private Long serviceId;

    @NotNull(message = "Le collaborateur est obligatoire")
    private Long employeeId;

    @NotNull(message = "La date est obligatoire")
    private LocalDate appointmentDate;

    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime startTime;

    private String notes;
}
