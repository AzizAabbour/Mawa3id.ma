package ma.mawa3id.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.mawa3id.domain.enums.NotificationChannel;
import ma.mawa3id.domain.enums.NotificationType;

/**
 * Customizable template for WhatsApp, SMS, and Email notifications.
 */
@Entity
@Table(name = "notification_templates", indexes = {
        @Index(name = "idx_notif_templates_business_id", columnList = "business_id"),
        @Index(name = "idx_notif_templates_type_channel", columnList = "business_id, type, channel, language", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationTemplate extends BaseEntity {

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 20)
    private NotificationChannel channel;

    @Column(name = "language", nullable = false, length = 10)
    @Builder.Default
    private String language = "fr"; // "fr", "ar", or "en"

    @Column(name = "subject", length = 255)
    private String subject;

    @Column(name = "body", columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;
}
