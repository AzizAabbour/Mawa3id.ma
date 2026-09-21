package ma.mawa3id.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataPointDto {
    private String label; // e.g. "Lun", "Mar", or "2026-09-21"
    private long count;
    private BigDecimal value; // for revenue charts
}
