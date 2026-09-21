package ma.mawa3id.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.domain.enums.NotificationChannel;
import ma.mawa3id.domain.enums.NotificationStatus;
import ma.mawa3id.domain.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private UUID publicId;
    private Long appointmentId;
    private Long customerId;
    private String recipient;
    private NotificationType type;
    private NotificationChannel channel;
    private NotificationStatus status;
    private String providerMessageId;
    private String messageContent;
    private Integer retryCount;
    private String errorMessage;
    private LocalDateTime sentAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime createdAt;
}
