package ma.mawa3id.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Employee linked to a business and a user account.
 */
@Entity
@Table(name = "employees", indexes = {
        @Index(name = "idx_employees_business_id", columnList = "business_id"),
        @Index(name = "idx_employees_user_id", columnList = "user_id"),
        @Index(name = "idx_employees_public_id", columnList = "public_id", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "color", length = 7)
    @Builder.Default
    private String color = "#1B5E3C";
}
