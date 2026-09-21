package ma.mawa3id.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.dto.appointment.AppointmentResponse;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatisticsDto {
    private long todayAppointmentsCount;
    private long upcomingAppointmentsCount;
    private long completedAppointmentsCount;
    private long cancelledAppointmentsCount;
    private long noShowCount;
    private double noShowRatePercentage;
    private long totalCustomersCount;
    private BigDecimal monthlyEstimatedRevenueMad;

    // Notifications stats
    private long whatsappSentCount;
    private long smsSentCount;
    private double notificationDeliveryRatePercentage;

    // Quick lists
    private List<AppointmentResponse> todayAppointments;
}
