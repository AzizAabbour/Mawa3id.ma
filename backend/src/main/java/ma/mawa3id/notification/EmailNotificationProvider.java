package ma.mawa3id.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.mawa3id.domain.enums.NotificationChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Email notification provider using Spring Mail.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationProvider implements NotificationProvider {

    private final JavaMailSender mailSender;

    @Value("${app.notification.email.enabled:false}")
    private boolean enabled;

    @Value("${app.notification.email.from:noreply@mawa3id.ma}")
    private String fromEmail;

    @Override
    public String send(NotificationMessage message) throws Exception {
        log.info("[EMAIL] Sending Email to recipient={}: Subject='{}'",
                message.getRecipient(), message.getSubject());

        if (enabled) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(message.getRecipient());
            mailMessage.setSubject(message.getSubject() != null ? message.getSubject() : "Rendez-vous Mawa3id.ma");
            mailMessage.setText(message.getContent());
            mailSender.send(mailMessage);
        } else {
            log.info("[EMAIL] [SIMULATION MODE] Email sent successfully to {}", message.getRecipient());
        }

        return "email_msg_" + UUID.randomUUID().toString().substring(0, 12);
    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
