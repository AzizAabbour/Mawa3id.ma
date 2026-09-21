package ma.mawa3id.repository;

import ma.mawa3id.domain.entity.NotificationTemplate;
import ma.mawa3id.domain.enums.NotificationChannel;
import ma.mawa3id.domain.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    List<NotificationTemplate> findByBusinessId(Long businessId);
    Optional<NotificationTemplate> findByBusinessIdAndTypeAndChannelAndLanguage(
            Long businessId, NotificationType type, NotificationChannel channel, String language);
}
