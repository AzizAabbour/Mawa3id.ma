package ma.mawa3id.service;

import lombok.RequiredArgsConstructor;
import ma.mawa3id.domain.entity.Appointment;
import ma.mawa3id.domain.enums.AppointmentStatus;
import ma.mawa3id.domain.enums.NotificationChannel;
import ma.mawa3id.domain.enums.NotificationStatus;
import ma.mawa3id.dto.appointment.AppointmentResponse;
import ma.mawa3id.dto.dashboard.ChartDataPointDto;
import ma.mawa3id.dto.dashboard.DashboardStatisticsDto;
import ma.mawa3id.mapper.AppointmentMapper;
import ma.mawa3id.repository.AppointmentRepository;
import ma.mawa3id.repository.CustomerRepository;
import ma.mawa3id.repository.NotificationRepository;
import ma.mawa3id.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final NotificationRepository notificationRepository;
    private final AppointmentMapper appointmentMapper;

    @Transactional(readOnly = true)
    public DashboardStatisticsDto getStatistics() {
        Long businessId = TenantContext.getCurrentBusinessId();
        LocalDate today = LocalDate.now();

        List<Appointment> todayAppointments = appointmentRepository.findByBusinessIdAndAppointmentDate(businessId, today);
        long todayCount = todayAppointments.size();
        long completedCount = appointmentRepository.countByBusinessIdAndStatus(businessId, AppointmentStatus.COMPLETED);
        long cancelledCount = appointmentRepository.countByBusinessIdAndStatus(businessId, AppointmentStatus.CANCELLED);
        long noShowCount = appointmentRepository.countByBusinessIdAndStatus(businessId, AppointmentStatus.NO_SHOW);
        long totalCustomers = customerRepository.countByBusinessId(businessId);

        // No-show rate calculation
        long totalPast = completedCount + noShowCount;
        double noShowRate = totalPast > 0 ? (double) noShowCount / totalPast * 100.0 : 0.0;

        // Revenue calculation for current month
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        List<Appointment> monthAppointments = appointmentRepository
                .findByBusinessIdAndAppointmentDateBetween(businessId, startOfMonth, endOfMonth);

        BigDecimal revenueMad = monthAppointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED || a.getStatus() == AppointmentStatus.CONFIRMED)
                .map(a -> a.getService().getPriceMad())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Notifications
        long waSent = notificationRepository.countByBusinessIdAndChannel(businessId, NotificationChannel.WHATSAPP);
        long smsSent = notificationRepository.countByBusinessIdAndChannel(businessId, NotificationChannel.SMS);
        long deliveredCount = notificationRepository.countByBusinessIdAndStatus(businessId, NotificationStatus.DELIVERED)
                + notificationRepository.countByBusinessIdAndStatus(businessId, NotificationStatus.SENT);
        long totalNotifs = waSent + smsSent;
        double deliveryRate = totalNotifs > 0 ? (double) deliveredCount / totalNotifs * 100.0 : 100.0;

        List<AppointmentResponse> todayList = appointmentMapper.toResponseList(todayAppointments);

        return DashboardStatisticsDto.builder()
                .todayAppointmentsCount(todayCount)
                .upcomingAppointmentsCount(todayAppointments.stream().filter(a -> a.getStatus() == AppointmentStatus.CONFIRMED).count())
                .completedAppointmentsCount(completedCount)
                .cancelledAppointmentsCount(cancelledCount)
                .noShowCount(noShowCount)
                .noShowRatePercentage(Math.round(noShowRate * 10.0) / 10.0)
                .totalCustomersCount(totalCustomers)
                .monthlyEstimatedRevenueMad(revenueMad)
                .whatsappSentCount(waSent)
                .smsSentCount(smsSent)
                .notificationDeliveryRatePercentage(Math.round(deliveryRate * 10.0) / 10.0)
                .todayAppointments(todayList)
                .build();
    }

    @Transactional(readOnly = true)
    public List<ChartDataPointDto> getWeeklyAppointmentsChart() {
        Long businessId = TenantContext.getCurrentBusinessId();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(6);

        List<ChartDataPointDto> points = new ArrayList<>();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE d", Locale.FRENCH);

        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            long count = appointmentRepository.countByBusinessIdAndAppointmentDate(businessId, date);
            points.add(ChartDataPointDto.builder()
                    .label(date.format(dayFormatter))
                    .count(count)
                    .build());
        }

        return points;
    }

    @Transactional(readOnly = true)
    public List<ChartDataPointDto> getMonthlyRevenueChart() {
        Long businessId = TenantContext.getCurrentBusinessId();
        LocalDate today = LocalDate.now();
        List<ChartDataPointDto> points = new ArrayList<>();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.FRENCH);

        for (int i = 5; i >= 0; i--) {
            LocalDate monthDate = today.minusMonths(i);
            LocalDate start = monthDate.withDayOfMonth(1);
            LocalDate end = monthDate.withDayOfMonth(monthDate.lengthOfMonth());

            List<Appointment> appts = appointmentRepository.findByBusinessIdAndAppointmentDateBetween(businessId, start, end);
            BigDecimal totalRev = appts.stream()
                    .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                    .map(a -> a.getService().getPriceMad())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            points.add(ChartDataPointDto.builder()
                    .label(monthDate.format(monthFormatter))
                    .value(totalRev)
                    .build());
        }

        return points;
    }
}
