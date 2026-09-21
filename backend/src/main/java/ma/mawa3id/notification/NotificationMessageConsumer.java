package ma.mawa3id.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.mawa3id.domain.entity.Notification;
import ma.mawa3id.domain.enums.NotificationChannel;
import ma.mawa3id.domain.enums.NotificationStatus;
import ma.mawa3id.repository.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationMessageConsumer {

    private final List<NotificationProvider> providers;
    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = "${app.rabbitmq.queue.send:mawa3id.notifications.send}")
    @Transactional
    public void processNotification(NotificationMessage message) {
        log.info("Processing notification message ID: {}, Channel: {}",
                message.getNotificationId(), message.getChannel());

        Notification notification = null;
        if (message.getNotificationId() != null) {
            notification = notificationRepository.findById(message.getNotificationId()).orElse(null);
        }

        if (notification != null) {
            notification.setStatus(NotificationStatus.PROCESSING);
            notificationRepository.save(notification);
        }

        NotificationProvider provider = getProviderForChannel(message.getChannel());
        if (provider == null) {
            log.error("No notification provider found for channel: {}", message.getChannel());
            if (notification != null) {
                notification.setStatus(NotificationStatus.FAILED);
                notification.setErrorMessage("Aucun fournisseur disponible pour le canal: " + message.getChannel());
                notificationRepository.save(notification);
            }
            return;
        }

        try {
            String messageId = provider.send(message);
            if (notification != null) {
                notification.setStatus(NotificationStatus.SENT);
                notification.setProviderMessageId(messageId);
                notification.setSentAt(LocalDateTime.now());
                notification.setDeliveredAt(LocalDateTime.now()); // In simulation mode, delivered immediately
                notificationRepository.save(notification);
            }
            log.info("Notification successfully dispatched. ProviderMsgId: {}", messageId);
        } catch (Exception ex) {
            log.error("Failed to send notification ID: {}", message.getNotificationId(), ex);
            if (notification != null) {
                int retry = notification.getRetryCount() + 1;
                notification.setRetryCount(retry);
                if (retry >= 3) {
                    notification.setStatus(NotificationStatus.FAILED);
                } else {
                    notification.setStatus(NotificationStatus.PENDING); // eligible for retry
                }
                notification.setErrorMessage(ex.getMessage());
                notificationRepository.save(notification);
            }
        }
    }

    private NotificationProvider getProviderForChannel(NotificationChannel channel) {
        return providers.stream()
                .filter(p -> p.getChannel() == channel)
                .findFirst()
                .orElse(null);
    }
}
