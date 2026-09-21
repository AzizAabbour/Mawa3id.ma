package ma.mawa3id.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.domain.enums.NotificationChannel;
import ma.mawa3id.domain.enums.NotificationType;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplateResponse {
    private Long id;
    private UUID publicId;
    private NotificationType type;
    private NotificationChannel channel;
    private String language;
    private String subject;
    private String body;
    private boolean active;
}
