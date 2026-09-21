package ma.mawa3id.repository;

import ma.mawa3id.domain.entity.Business;
import ma.mawa3id.domain.enums.BusinessCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
    Optional<Business> findBySlug(String slug);
    Optional<Business> findByPublicId(UUID publicId);
    Optional<Business> findByOwnerId(Long ownerId);
    boolean existsBySlug(String slug);

    @Query("SELECT b FROM Business b WHERE " +
           "(:city IS NULL OR LOWER(b.city) = LOWER(:city)) AND " +
           "(:category IS NULL OR b.category = :category) AND " +
           "(:query IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Business> searchBusinesses(
            @Param("city") String city,
            @Param("category") BusinessCategory category,
            @Param("query") String query,
            Pageable pageable);
}
