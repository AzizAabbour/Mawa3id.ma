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
public class AdminDashboardStatsDto {
    private long totalBusinesses;
    private long activeSubscriptions;
    private BigDecimal monthlyRecurringRevenueMad;
    private long newRegistrationsThisMonth;
    private long totalAppointmentsPlatformWide;
    private long totalNotificationsSent;
    private long totalFailedNotifications;
    private long totalSmsUsed;
    private long totalWhatsappUsed;
}
