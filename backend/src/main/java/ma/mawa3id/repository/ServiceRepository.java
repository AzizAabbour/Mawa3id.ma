package ma.mawa3id.repository;

import ma.mawa3id.domain.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByBusinessIdAndActiveTrue(Long businessId);
    List<Service> findByBusinessId(Long businessId);
    Optional<Service> findByPublicIdAndBusinessId(UUID publicId, Long businessId);
    Optional<Service> findByPublicId(UUID publicId);
    long countByBusinessIdAndActiveTrue(Long businessId);
}
