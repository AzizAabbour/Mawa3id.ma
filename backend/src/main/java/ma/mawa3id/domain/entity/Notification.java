package ma.mawa3id.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.mawa3id.domain.enums.NotificationChannel;
import ma.mawa3id.domain.enums.NotificationStatus;
import ma.mawa3id.domain.enums.NotificationType;

import java.time.LocalDateTime;

/**
 * Notification record for SMS/WhatsApp/Email delivery tracking.
 */
@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notifications_business_id", columnList = "business_id"),
        @Index(name = "idx_notifications_appointment_id", columnList = "appointment_id"),
        @Index(name = "idx_notifications_status", columnList = "status"),
        @Index(name = "idx_notifications_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "appointment_id")
    private Long appointmentId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "recipient", nullable = false, length = 255)
    private String recipient; // Phone or Email

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 20)
    private NotificationChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "provider_message_id", length = 100)
    private String providerMessageId;

    @Column(name = "message_content", columnDefinition = "TEXT", nullable = false)
    private String messageContent;

    @Column(name = "retry_count", nullable = false)
    @Builder.Default
    private Integer retryCount = 0;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;
}
