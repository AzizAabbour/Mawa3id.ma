package ma.mawa3id.notification;

import ma.mawa3id.domain.enums.NotificationChannel;

/**
 * Common abstraction for all notification delivery providers.
 * Decouples WhatsApp, SMS, and Email integrations from the core business logic.
 */
public interface NotificationProvider {

    /**
     * Sends the notification message asynchronously or synchronously.
     * @param message the notification message details
     * @return provider message identifier (or null if not returned)
     * @throws Exception if sending fails
     */
    String send(NotificationMessage message) throws Exception;

    /**
     * Gets the channel supported by this provider.
     */
    NotificationChannel getChannel();

    /**
     * Whether this provider is configured and available.
     */
    boolean isAvailable();
}
