package ma.mawa3id.mapper;

import ma.mawa3id.domain.entity.Notification;
import ma.mawa3id.domain.entity.NotificationConfig;
import ma.mawa3id.domain.entity.NotificationTemplate;
import ma.mawa3id.dto.notification.NotificationConfigDto;
import ma.mawa3id.dto.notification.NotificationResponse;
import ma.mawa3id.dto.notification.NotificationTemplateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {
    NotificationResponse toResponse(Notification notification);
    List<NotificationResponse> toResponseList(List<Notification> notifications);
    NotificationTemplateResponse toResponse(NotificationTemplate template);
    List<NotificationTemplateResponse> toTemplateResponseList(List<NotificationTemplate> templates);
    NotificationConfigDto toDto(NotificationConfig config);
}
