package ma.mawa3id.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.mawa3id.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment record for subscription or service payments in MAD.
 */
@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payments_business_id", columnList = "business_id"),
        @Index(name = "idx_payments_subscription_id", columnList = "subscription_id"),
        @Index(name = "idx_payments_status", columnList = "status"),
        @Index(name = "idx_payments_transaction_id", columnList = "provider_transaction_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "amount_mad", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountMad;

    @Column(name = "currency", nullable = false, length = 5)
    @Builder.Default
    private String currency = "MAD";

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "provider_name", length = 50)
    private String providerName; // e.g. "CMI", "PAYZONE", "CASHPLUS", "STRIPE_TEST"

    @Column(name = "provider_transaction_id", length = 150)
    private String providerTransactionId;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod; // "CARD", "CASH_PLUS", "BANK_TRANSFER"

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
}
