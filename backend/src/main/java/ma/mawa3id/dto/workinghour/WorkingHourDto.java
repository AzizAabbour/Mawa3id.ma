package ma.mawa3id.dto.workinghour;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkingHourDto {
    private Long id;
    private Long employeeId;
    @NotNull(message = "Le jour de la semaine est obligatoire")
    private DayOfWeek dayOfWeek;
    @NotNull(message = "L'heure d'ouverture est obligatoire")
    private LocalTime startTime;
    @NotNull(message = "L'heure de fermeture est obligatoire")
    private LocalTime endTime;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;
    private boolean dayOff;
}
