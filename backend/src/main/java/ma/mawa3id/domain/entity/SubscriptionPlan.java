package ma.mawa3id.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;

/**
 * SaaS subscription plan definition (e.g., FREE, STARTER, BUSINESS, PRO).
 */
@Entity
@Table(name = "subscription_plans", indexes = {
        @Index(name = "idx_subscription_plans_slug", columnList = "slug", unique = true),
        @Index(name = "idx_subscription_plans_active", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlan extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "slug", unique = true, nullable = false, length = 50)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price_monthly_mad", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceMonthlyMad;

    @Column(name = "max_appointments_per_month", nullable = false)
    private Integer maxAppointmentsPerMonth; // -1 for unlimited

    @Column(name = "max_customers", nullable = false)
    private Integer maxCustomers; // -1 for unlimited

    @Column(name = "max_employees", nullable = false)
    private Integer maxEmployees; // -1 for unlimited

    @Column(name = "whatsapp_notifications_included", nullable = false)
    @Builder.Default
    private boolean whatsappNotificationsIncluded = true;

    @Column(name = "sms_notifications_included", nullable = false)
    @Builder.Default
    private boolean smsNotificationsIncluded = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "features", columnDefinition = "jsonb")
    private List<String> features;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;
}
