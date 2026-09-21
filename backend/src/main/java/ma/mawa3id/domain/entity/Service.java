package ma.mawa3id.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Service offered by a business (e.g., Haircut, Beard Trim, Dental Cleaning).
 */
@Entity
@Table(name = "services", indexes = {
        @Index(name = "idx_services_business_id", columnList = "business_id"),
        @Index(name = "idx_services_public_id", columnList = "public_id", unique = true),
        @Index(name = "idx_services_active", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service extends BaseEntity {

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "price_mad", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceMad;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "color", length = 7)
    @Builder.Default
    private String color = "#1B5E3C";

    @ManyToMany
    @JoinTable(
            name = "service_employees",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    @Builder.Default
    private Set<Employee> assignedEmployees = new HashSet<>();
}
