package ma.mawa3id.repository;

import ma.mawa3id.domain.entity.Subscription;
import ma.mawa3id.domain.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByBusinessId(Long businessId);
    long countByStatus(SubscriptionStatus status);
}
