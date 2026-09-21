package ma.mawa3id.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.mawa3id.domain.enums.Role;

/**
 * Platform user — can be a super admin, business owner, employee, or customer.
 * Multi-tenant: users are scoped to a business (except SUPER_ADMIN).
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true),
        @Index(name = "idx_users_phone", columnList = "phone"),
        @Index(name = "idx_users_business_id", columnList = "business_id"),
        @Index(name = "idx_users_public_id", columnList = "public_id", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role;

    @Column(name = "business_id")
    private Long businessId;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private boolean emailVerified = false;

    @Column(name = "email_verification_token")
    private String emailVerificationToken;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_expires_at")
    private java.time.LocalDateTime passwordResetExpiresAt;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "language", length = 5)
    @Builder.Default
    private String language = "fr";

    /**
     * Returns the full name.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
