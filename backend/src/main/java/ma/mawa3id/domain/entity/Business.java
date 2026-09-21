package ma.mawa3id.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.mawa3id.domain.enums.BusinessCategory;

/**
 * Business tenant — the core SaaS workspace.
 * Each business has its own customers, services, appointments, and notifications.
 */
@Entity
@Table(name = "businesses", indexes = {
        @Index(name = "idx_businesses_slug", columnList = "slug", unique = true),
        @Index(name = "idx_businesses_owner_id", columnList = "owner_id"),
        @Index(name = "idx_businesses_public_id", columnList = "public_id", unique = true),
        @Index(name = "idx_businesses_city", columnList = "city"),
        @Index(name = "idx_businesses_category", columnList = "category")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Business extends BaseEntity {

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "slug", unique = true, nullable = false, length = 100)
    private String slug;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "whatsapp_number", length = 20)
    private String whatsappNumber;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "google_maps_url")
    private String googleMapsUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 30)
    private BusinessCategory category;

    @Column(name = "timezone", length = 50)
    @Builder.Default
    private String timezone = "Africa/Casablanca";

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "cover_image_url")
    private String coverImageUrl;
}
