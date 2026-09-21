package ma.mawa3id.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationMessagePublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange:mawa3id.notifications}")
    private String exchangeName;

    @Value("${app.rabbitmq.routing-key.send:notification.send}")
    private String routingKey;

    public void publish(NotificationMessage message) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
            log.info("Published notification message #{} to queue for recipient {}",
                    message.getNotificationId(), message.getRecipient());
        } catch (Exception ex) {
            log.error("Failed to publish notification to RabbitMQ, proceeding synchronously as fallback", ex);
        }
    }
}
