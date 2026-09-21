package ma.mawa3id.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.mawa3id.domain.entity.Appointment;
import ma.mawa3id.domain.enums.NotificationType;
import ma.mawa3id.repository.AppointmentRepository;
import ma.mawa3id.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Scheduled job executing periodic checks for automated 24-hour and 2-hour appointment reminders.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderScheduler {

    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    /**
     * Checks every 15 minutes for appointments happening tomorrow (approx 24h reminder)
     * and in 2 hours (2h reminder).
     */
    @Scheduled(cron = "0 */15 * * * *")
    public void scheduleReminders() {
        log.info("Running automated appointment reminder check...");

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalTime nowTime = LocalTime.now();
        LocalTime windowStart = nowTime.minusMinutes(7);
        LocalTime windowEnd = nowTime.plusMinutes(8);

        // 24H Reminders
        try {
            List<Appointment> tomorrowAppointments =
                    appointmentRepository.findUpcomingForReminder(tomorrow, windowStart, windowEnd);

            for (Appointment appointment : tomorrowAppointments) {
                log.info("Sending 24h reminder for appointment ID #{}", appointment.getId());
                notificationService.sendAppointmentNotification(appointment, NotificationType.REMINDER_24H);
            }
        } catch (Exception e) {
            log.error("Error executing 24h reminders", e);
        }

        // 2H Reminders (today in 2 hours)
        try {
            LocalDate today = LocalDate.now();
            LocalTime twoHoursLater = nowTime.plusHours(2);
            LocalTime twoHourWindowStart = twoHoursLater.minusMinutes(7);
            LocalTime twoHourWindowEnd = twoHoursLater.plusMinutes(8);

            List<Appointment> twoHourAppointments =
                    appointmentRepository.findUpcomingForReminder(today, twoHourWindowStart, twoHourWindowEnd);

            for (Appointment appointment : twoHourAppointments) {
                log.info("Sending 2h reminder for appointment ID #{}", appointment.getId());
                notificationService.sendAppointmentNotification(appointment, NotificationType.REMINDER_2H);
            }
        } catch (Exception e) {
            log.error("Error executing 2h reminders", e);
        }
    }
}
