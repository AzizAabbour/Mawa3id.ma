package ma.mawa3id.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.domain.enums.NotificationChannel;
import ma.mawa3id.domain.enums.NotificationType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplateRequest {
    @NotNull(message = "Le type de notification est obligatoire")
    private NotificationType type;

    @NotNull(message = "Le canal de notification est obligatoire")
    private NotificationChannel channel;

    private String language = "fr";

    private String subject;

    @NotBlank(message = "Le corps du message est obligatoire")
    private String body;

    private boolean active = true;
}
