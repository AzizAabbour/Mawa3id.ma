package ma.mawa3id.android.model;

import java.math.BigDecimal;

public class DashboardStats {
    private long todayAppointmentsCount;
    private long upcomingAppointmentsCount;
    private long completedAppointmentsCount;
    private long cancelledAppointmentsCount;
    private long noShowCount;
    private double noShowRatePercentage;
    private long totalCustomersCount;
    private BigDecimal monthlyEstimatedRevenueMad;
    private long whatsappSentCount;

    public long getTodayAppointmentsCount() { return todayAppointmentsCount; }
    public long getUpcomingAppointmentsCount() { return upcomingAppointmentsCount; }
    public long getCompletedAppointmentsCount() { return completedAppointmentsCount; }
    public long getCancelledAppointmentsCount() { return cancelledAppointmentsCount; }
    public long getNoShowCount() { return noShowCount; }
    public double getNoShowRatePercentage() { return noShowRatePercentage; }
    public long getTotalCustomersCount() { return totalCustomersCount; }
    public BigDecimal getMonthlyEstimatedRevenueMad() { return monthlyEstimatedRevenueMad; }
    public long getWhatsappSentCount() { return whatsappSentCount; }
}
