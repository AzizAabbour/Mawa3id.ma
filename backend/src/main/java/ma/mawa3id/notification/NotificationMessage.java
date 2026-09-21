package ma.mawa3id.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.domain.enums.NotificationChannel;
import ma.mawa3id.domain.enums.NotificationType;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage implements Serializable {
    private Long notificationId;
    private Long businessId;
    private Long appointmentId;
    private Long customerId;
    private String recipient;
    private NotificationChannel channel;
    private NotificationType type;
    private String content;
    private String subject;
    private String customerName;
    private String businessName;
    private String serviceName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String businessAddress;
    private String businessPhone;
}
