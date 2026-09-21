package ma.mawa3id.repository;

import ma.mawa3id.domain.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    Page<AuditLog> findByBusinessId(Long businessId, Pageable pageable);
    Page<AuditLog> findByUserId(Long userId, Pageable pageable);
}
