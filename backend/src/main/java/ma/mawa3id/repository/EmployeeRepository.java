package ma.mawa3id.repository;

import ma.mawa3id.domain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByBusinessIdAndActiveTrue(Long businessId);
    List<Employee> findByBusinessId(Long businessId);
    Optional<Employee> findByPublicIdAndBusinessId(UUID publicId, Long businessId);
    Optional<Employee> findByUserIdAndBusinessId(Long userId, Long businessId);
    long countByBusinessIdAndActiveTrue(Long businessId);
}
