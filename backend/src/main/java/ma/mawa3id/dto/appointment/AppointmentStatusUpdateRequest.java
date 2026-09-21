package ma.mawa3id.dto.appointment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.domain.enums.AppointmentStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatusUpdateRequest {
    @NotNull(message = "Le statut est obligatoire")
    private AppointmentStatus status;
    private String cancellationReason;
}
