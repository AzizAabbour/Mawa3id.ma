package ma.mawa3id.repository;

import ma.mawa3id.domain.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    Optional<SubscriptionPlan> findBySlug(String slug);
    List<SubscriptionPlan> findByActiveTrueOrderByDisplayOrderAsc();
}
