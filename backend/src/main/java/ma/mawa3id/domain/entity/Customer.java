package ma.mawa3id.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Customer profile for a specific business.
 */
@Entity
@Table(name = "customers", indexes = {
        @Index(name = "idx_customers_business_id", columnList = "business_id"),
        @Index(name = "idx_customers_phone", columnList = "phone"),
        @Index(name = "idx_customers_public_id", columnList = "public_id", unique = true)
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_customer_business_phone", columnNames = {"business_id", "phone"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseEntity {

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "whatsapp_number", length = 20)
    private String whatsappNumber;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "total_appointments", nullable = false)
    @Builder.Default
    private Integer totalAppointments = 0;

    @Column(name = "total_spent_mad", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalSpentMad = BigDecimal.ZERO;

    @Column(name = "last_appointment_at")
    private LocalDateTime lastAppointmentAt;
}
