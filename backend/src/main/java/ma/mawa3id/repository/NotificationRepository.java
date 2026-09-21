package ma.mawa3id.repository;

import ma.mawa3id.domain.entity.Notification;
import ma.mawa3id.domain.enums.NotificationChannel;
import ma.mawa3id.domain.enums.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByBusinessId(Long businessId, Pageable pageable);
    List<Notification> findByStatusAndRetryCountLessThan(NotificationStatus status, int maxRetries);
    long countByBusinessIdAndStatus(Long businessId, NotificationStatus status);
    long countByBusinessIdAndChannel(Long businessId, NotificationChannel channel);
}
