package ma.mawa3id.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailableSlotDto {
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean available;
    private Long employeeId;
    private String employeeName;
}
