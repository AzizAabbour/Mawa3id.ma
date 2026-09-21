package ma.mawa3id.repository;

import ma.mawa3id.domain.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByBusinessIdAndPhone(Long businessId, String phone);
    Optional<Customer> findByPublicIdAndBusinessId(UUID publicId, Long businessId);
    long countByBusinessId(Long businessId);

    @Query("SELECT c FROM Customer c WHERE c.businessId = :businessId AND " +
           "(:query IS NULL OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "c.phone LIKE CONCAT('%', :query, '%') OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Customer> searchCustomers(
            @Param("businessId") Long businessId,
            @Param("query") String query,
            Pageable pageable);
}
