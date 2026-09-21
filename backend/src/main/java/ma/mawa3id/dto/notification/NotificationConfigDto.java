package ma.mawa3id.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationConfigDto {
    private boolean whatsappEnabled;
    private boolean smsEnabled;
    private boolean emailEnabled;
    private boolean reminder24hEnabled;
    private boolean reminder2hEnabled;
    private Integer customReminderMinutes;
    private String defaultLanguage;
}
