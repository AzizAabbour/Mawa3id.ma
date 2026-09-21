package ma.mawa3id.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.mawa3id.domain.entity.*;
import ma.mawa3id.domain.enums.NotificationChannel;
import ma.mawa3id.domain.enums.NotificationStatus;
import ma.mawa3id.domain.enums.NotificationType;
import ma.mawa3id.dto.notification.*;
import ma.mawa3id.exception.ResourceNotFoundException;
import ma.mawa3id.mapper.NotificationMapper;
import ma.mawa3id.repository.BusinessRepository;
import ma.mawa3id.repository.NotificationConfigRepository;
import ma.mawa3id.repository.NotificationRepository;
import ma.mawa3id.repository.NotificationTemplateRepository;
import ma.mawa3id.tenant.TenantContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final NotificationConfigRepository configRepository;
    private final BusinessRepository businessRepository;
    private final NotificationTemplateEngine templateEngine;
    private final NotificationMessagePublisher publisher;
    private final NotificationMapper notificationMapper;

    @Transactional
    public void sendAppointmentNotification(Appointment appointment, NotificationType type) {
        Long businessId = appointment.getBusinessId();
        Business business = businessRepository.findById(businessId).orElse(null);
        if (business == null) return;

        NotificationConfig config = configRepository.findByBusinessId(businessId)
                .orElseGet(() -> NotificationConfig.builder()
                        .businessId(businessId)
                        .whatsappEnabled(true)
                        .smsEnabled(false)
                        .emailEnabled(false)
                        .reminder24hEnabled(true)
                        .reminder2hEnabled(true)
                        .defaultLanguage("fr")
                        .build());

        String lang = config.getDefaultLanguage() != null ? config.getDefaultLanguage() : "fr";
        Customer customer = appointment.getCustomer();

        // 1. WhatsApp Notification
        if (config.isWhatsappEnabled() && customer.getPhone() != null) {
            dispatchForChannel(appointment, business, customer, type, NotificationChannel.WHATSAPP, lang);
        }

        // 2. SMS Notification
        if (config.isSmsEnabled() && customer.getPhone() != null) {
            dispatchForChannel(appointment, business, customer, type, NotificationChannel.SMS, lang);
        }

        // 3. Email Notification
        if (config.isEmailEnabled() && customer.getEmail() != null) {
            dispatchForChannel(appointment, business, customer, type, NotificationChannel.EMAIL, lang);
        }
    }

    private void dispatchForChannel(Appointment appointment, Business business, Customer customer,
                                   NotificationType type, NotificationChannel channel, String lang) {
        String templateBody = getTemplateBody(business.getId(), type, channel, lang);

        NotificationMessage message = NotificationMessage.builder()
                .businessId(business.getId())
                .appointmentId(appointment.getId())
                .customerId(customer.getId())
                .recipient(channel == NotificationChannel.EMAIL ? customer.getEmail() : customer.getPhone())
                .channel(channel)
                .type(type)
                .customerName(customer.getFullName())
                .businessName(business.getName())
                .serviceName(appointment.getService().getName())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getStartTime())
                .businessAddress(business.getAddress())
                .businessPhone(business.getPhone())
                .subject(getSubjectForType(type, business.getName()))
                .build();

        String renderedContent = templateEngine.render(templateBody, message);
        message.setContent(renderedContent);

        Notification notification = Notification.builder()
                .businessId(business.getId())
                .appointmentId(appointment.getId())
                .customerId(customer.getId())
                .recipient(message.getRecipient())
                .type(type)
                .channel(channel)
                .status(NotificationStatus.PENDING)
                .messageContent(renderedContent)
                .createdAt(LocalDateTime.now())
                .build();

        notification = notificationRepository.save(notification);
        message.setNotificationId(notification.getId());

        publisher.publish(message);
    }

    private String getTemplateBody(Long businessId, NotificationType type, NotificationChannel channel, String lang) {
        Optional<NotificationTemplate> custom = templateRepository
                .findByBusinessIdAndTypeAndChannelAndLanguage(businessId, type, channel, lang);
        if (custom.isPresent() && custom.get().isActive()) {
            return custom.get().getBody();
        }
        return getDefaultTemplate(type, channel, lang);
    }

    private String getDefaultTemplate(NotificationType type, NotificationChannel channel, String lang) {
        return switch (type) {
            case BOOKING_CONFIRMATION ->
                    "Bonjour {{customerName}},\n\nVotre rendez-vous chez {{businessName}} est confirmé !\n\nPrestation : {{serviceName}}\nDate : {{appointmentDate}}\nHeure : {{appointmentTime}}\nAdresse : {{businessAddress}}\n\nÀ très bientôt !";
            case REMINDER_24H ->
                    "Rappel Mawa3id.ma : Bonjour {{customerName}}, vous avez rendez-vous demain chez {{businessName}} à {{appointmentTime}} pour votre prestation {{serviceName}}.\nAdresse : {{businessAddress}}.";
            case REMINDER_2H ->
                    "Rappel : Votre rendez-vous chez {{businessName}} aura lieu dans 2 heures (à {{appointmentTime}}). Nous vous attendons avec plaisir !";
            case CANCELLATION ->
                    "Bonjour {{customerName}}, votre rendez-vous du {{appointmentDate}} à {{appointmentTime}} chez {{businessName}} a été annulé.";
            case RESCHEDULE ->
                    "Bonjour {{customerName}}, votre rendez-vous chez {{businessName}} a été reprogrammé au {{appointmentDate}} à {{appointmentTime}}.";
            case WELCOME ->
                    "Bienvenue {{customerName}} chez {{businessName}} ! Merci pour votre fidélité.";
        };
    }

    private String getSubjectForType(NotificationType type, String businessName) {
        return switch (type) {
            case BOOKING_CONFIRMATION -> "Confirmation de votre rendez-vous - " + businessName;
            case REMINDER_24H, REMINDER_2H -> "Rappel de votre rendez-vous - " + businessName;
            case CANCELLATION -> "Annulation de votre rendez-vous - " + businessName;
            case RESCHEDULE -> "Modification de votre rendez-vous - " + businessName;
            case WELCOME -> "Bienvenue chez " + businessName;
        };
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotifications(Pageable pageable) {
        Long businessId = TenantContext.getCurrentBusinessId();
        return notificationRepository.findByBusinessId(businessId, pageable)
                .map(notificationMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getTemplates() {
        Long businessId = TenantContext.getCurrentBusinessId();
        return notificationMapper.toTemplateResponseList(templateRepository.findByBusinessId(businessId));
    }

    @Transactional
    public NotificationTemplateResponse saveTemplate(NotificationTemplateRequest request) {
        Long businessId = TenantContext.getCurrentBusinessId();
        NotificationTemplate template = templateRepository
                .findByBusinessIdAndTypeAndChannelAndLanguage(
                        businessId, request.getType(), request.getChannel(), request.getLanguage())
                .orElseGet(() -> NotificationTemplate.builder()
                        .businessId(businessId)
                        .type(request.getType())
                        .channel(request.getChannel())
                        .language(request.getLanguage())
                        .build());

        template.setSubject(request.getSubject());
        template.setBody(request.getBody());
        template.setActive(request.isActive());

        return notificationMapper.toResponse(templateRepository.save(template));
    }

    @Transactional(readOnly = true)
    public NotificationConfigDto getConfig() {
        Long businessId = TenantContext.getCurrentBusinessId();
        NotificationConfig config = configRepository.findByBusinessId(businessId)
                .orElseGet(() -> NotificationConfig.builder().businessId(businessId).build());
        return notificationMapper.toDto(config);
    }

    @Transactional
    public NotificationConfigDto updateConfig(NotificationConfigDto dto) {
        Long businessId = TenantContext.getCurrentBusinessId();
        NotificationConfig config = configRepository.findByBusinessId(businessId)
                .orElseGet(() -> NotificationConfig.builder().businessId(businessId).build());

        config.setWhatsappEnabled(dto.isWhatsappEnabled());
        config.setSmsEnabled(dto.isSmsEnabled());
        config.setEmailEnabled(dto.isEmailEnabled());
        config.setReminder24hEnabled(dto.isReminder24hEnabled());
        config.setReminder2hEnabled(dto.isReminder2hEnabled());
        config.setCustomReminderMinutes(dto.getCustomReminderMinutes());
        config.setDefaultLanguage(dto.getDefaultLanguage() != null ? dto.getDefaultLanguage() : "fr");

        return notificationMapper.toDto(configRepository.save(config));
    }

    public String previewTemplate(TemplatePreviewRequest request) {
        Map<String, String> sampleValues = new HashMap<>();
        sampleValues.put("customerName", request.getSampleCustomerName() != null ? request.getSampleCustomerName() : "Karim Bennani");
        sampleValues.put("businessName", request.getSampleBusinessName() != null ? request.getSampleBusinessName() : "Salon Riad Beauty");
        sampleValues.put("serviceName", request.getSampleServiceName() != null ? request.getSampleServiceName() : "Coupe & Barbe Traditionnelle");
        sampleValues.put("appointmentDate", request.getSampleDate() != null ? request.getSampleDate() : "Lundi 22 Septembre 2026");
        sampleValues.put("appointmentTime", request.getSampleTime() != null ? request.getSampleTime() : "14:30");
        sampleValues.put("businessAddress", request.getSampleBusinessAddress() != null ? request.getSampleBusinessAddress() : "12 Rue de Fès, Casablanca");
        sampleValues.put("businessPhone", "+212 5 22 12 34 56");

        return templateEngine.renderPreview(request.getTemplateBody(), sampleValues);
    }
}
