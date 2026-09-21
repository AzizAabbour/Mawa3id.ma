package ma.mawa3id.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Notification configuration per business.
 */
@Entity
@Table(name = "notification_configs", indexes = {
        @Index(name = "idx_notif_configs_business_id", columnList = "business_id", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationConfig extends BaseEntity {

    @Column(name = "business_id", nullable = false, unique = true)
    private Long businessId;

    @Column(name = "whatsapp_enabled", nullable = false)
    @Builder.Default
    private boolean whatsappEnabled = true;

    @Column(name = "sms_enabled", nullable = false)
    @Builder.Default
    private boolean smsEnabled = false;

    @Column(name = "email_enabled", nullable = false)
    @Builder.Default
    private boolean emailEnabled = false;

    @Column(name = "reminder_24h_enabled", nullable = false)
    @Builder.Default
    private boolean reminder24hEnabled = true;

    @Column(name = "reminder_2h_enabled", nullable = false)
    @Builder.Default
    private boolean reminder2hEnabled = true;

    @Column(name = "custom_reminder_minutes")
    private Integer customReminderMinutes;

    @Column(name = "default_language", length = 10)
    @Builder.Default
    private String defaultLanguage = "fr";
}
