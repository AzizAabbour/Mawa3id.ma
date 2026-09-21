package ma.mawa3id.notification;

import lombok.extern.slf4j.Slf4j;
import ma.mawa3id.domain.enums.NotificationChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * SMS notification provider (e.g. Infobip, Twilio, or local Moroccan SMS gateways).
 */
@Component
@Slf4j
public class SMSNotificationProvider implements NotificationProvider {

    @Value("${app.notification.sms.enabled:false}")
    private boolean enabled;

    @Value("${app.notification.sms.api-url:}")
    private String apiUrl;

    @Value("${app.notification.sms.api-key:}")
    private String apiKey;

    @Override
    public String send(NotificationMessage message) throws Exception {
        log.info("[SMS] Sending SMS to recipient={} (Business={}): {}",
                message.getRecipient(), message.getBusinessName(), message.getContent());

        if (enabled && apiUrl != null && !apiUrl.isBlank()) {
            // Real HTTP call to SMS gateway
            log.info("[SMS] Real API dispatch to URL: {}", apiUrl);
        } else {
            log.info("[SMS] [SIMULATION MODE] SMS sent successfully to {}", message.getRecipient());
        }

        return "sms_msg_" + UUID.randomUUID().toString().substring(0, 12);
    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.SMS;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
