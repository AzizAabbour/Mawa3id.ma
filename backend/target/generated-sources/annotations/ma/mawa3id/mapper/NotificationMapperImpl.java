package ma.mawa3id.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import ma.mawa3id.domain.entity.Notification;
import ma.mawa3id.domain.entity.NotificationConfig;
import ma.mawa3id.domain.entity.NotificationTemplate;
import ma.mawa3id.dto.notification.NotificationConfigDto;
import ma.mawa3id.dto.notification.NotificationResponse;
import ma.mawa3id.dto.notification.NotificationTemplateResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-21T16:14:26+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 21.0.12.1 (Eclipse Adoptium)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationResponse toResponse(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationResponse.NotificationResponseBuilder notificationResponse = NotificationResponse.builder();

        notificationResponse.appointmentId( notification.getAppointmentId() );
        notificationResponse.channel( notification.getChannel() );
        notificationResponse.createdAt( notification.getCreatedAt() );
        notificationResponse.customerId( notification.getCustomerId() );
        notificationResponse.deliveredAt( notification.getDeliveredAt() );
        notificationResponse.errorMessage( notification.getErrorMessage() );
        notificationResponse.id( notification.getId() );
        notificationResponse.messageContent( notification.getMessageContent() );
        notificationResponse.providerMessageId( notification.getProviderMessageId() );
        notificationResponse.publicId( notification.getPublicId() );
        notificationResponse.recipient( notification.getRecipient() );
        notificationResponse.retryCount( notification.getRetryCount() );
        notificationResponse.sentAt( notification.getSentAt() );
        notificationResponse.status( notification.getStatus() );
        notificationResponse.type( notification.getType() );

        return notificationResponse.build();
    }

    @Override
    public List<NotificationResponse> toResponseList(List<Notification> notifications) {
        if ( notifications == null ) {
            return null;
        }

        List<NotificationResponse> list = new ArrayList<NotificationResponse>( notifications.size() );
        for ( Notification notification : notifications ) {
            list.add( toResponse( notification ) );
        }

        return list;
    }

    @Override
    public NotificationTemplateResponse toResponse(NotificationTemplate template) {
        if ( template == null ) {
            return null;
        }

        NotificationTemplateResponse.NotificationTemplateResponseBuilder notificationTemplateResponse = NotificationTemplateResponse.builder();

        notificationTemplateResponse.active( template.isActive() );
        notificationTemplateResponse.body( template.getBody() );
        notificationTemplateResponse.channel( template.getChannel() );
        notificationTemplateResponse.id( template.getId() );
        notificationTemplateResponse.language( template.getLanguage() );
        notificationTemplateResponse.publicId( template.getPublicId() );
        notificationTemplateResponse.subject( template.getSubject() );
        notificationTemplateResponse.type( template.getType() );

        return notificationTemplateResponse.build();
    }

    @Override
    public List<NotificationTemplateResponse> toTemplateResponseList(List<NotificationTemplate> templates) {
        if ( templates == null ) {
            return null;
        }

        List<NotificationTemplateResponse> list = new ArrayList<NotificationTemplateResponse>( templates.size() );
        for ( NotificationTemplate notificationTemplate : templates ) {
            list.add( toResponse( notificationTemplate ) );
        }

        return list;
    }

    @Override
    public NotificationConfigDto toDto(NotificationConfig config) {
        if ( config == null ) {
            return null;
        }

        NotificationConfigDto.NotificationConfigDtoBuilder notificationConfigDto = NotificationConfigDto.builder();

        notificationConfigDto.customReminderMinutes( config.getCustomReminderMinutes() );
        notificationConfigDto.defaultLanguage( config.getDefaultLanguage() );
        notificationConfigDto.emailEnabled( config.isEmailEnabled() );
        notificationConfigDto.reminder24hEnabled( config.isReminder24hEnabled() );
        notificationConfigDto.reminder2hEnabled( config.isReminder2hEnabled() );
        notificationConfigDto.smsEnabled( config.isSmsEnabled() );
        notificationConfigDto.whatsappEnabled( config.isWhatsappEnabled() );

        return notificationConfigDto.build();
    }
}
