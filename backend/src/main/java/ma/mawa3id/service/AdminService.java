package ma.mawa3id.service;

import lombok.RequiredArgsConstructor;
import ma.mawa3id.domain.entity.Business;
import ma.mawa3id.domain.enums.NotificationChannel;
import ma.mawa3id.domain.enums.NotificationStatus;
import ma.mawa3id.domain.enums.SubscriptionStatus;
import ma.mawa3id.dto.business.BusinessResponse;
import ma.mawa3id.dto.dashboard.AdminDashboardStatsDto;
import ma.mawa3id.exception.ResourceNotFoundException;
import ma.mawa3id.mapper.BusinessMapper;
import ma.mawa3id.repository.AppointmentRepository;
import ma.mawa3id.repository.BusinessRepository;
import ma.mawa3id.repository.NotificationRepository;
import ma.mawa3id.repository.SubscriptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final BusinessRepository businessRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AppointmentRepository appointmentRepository;
    private final NotificationRepository notificationRepository;
    private final BusinessMapper businessMapper;

    @Transactional(readOnly = true)
    public AdminDashboardStatsDto getPlatformStats() {
        long totalBusinesses = businessRepository.count();
        long activeSubs = subscriptionRepository.countByStatus(SubscriptionStatus.ACTIVE);
        long totalAppointments = appointmentRepository.count();
        long totalNotifications = notificationRepository.count();
        long failedNotifications = notificationRepository.findAll().stream()
                .filter(n -> n.getStatus() == NotificationStatus.FAILED).count();
        long totalSms = notificationRepository.findAll().stream()
                .filter(n -> n.getChannel() == NotificationChannel.SMS).count();
        long totalWhatsapp = notificationRepository.findAll().stream()
                .filter(n -> n.getChannel() == NotificationChannel.WHATSAPP).count();

        // Approximate MRR in MAD
        BigDecimal mrr = BigDecimal.valueOf(activeSubs).multiply(BigDecimal.valueOf(149.00));

        return AdminDashboardStatsDto.builder()
                .totalBusinesses(totalBusinesses)
                .activeSubscriptions(activeSubs)
                .monthlyRecurringRevenueMad(mrr)
                .totalAppointmentsPlatformWide(totalAppointments)
                .totalNotificationsSent(totalNotifications)
                .totalFailedNotifications(failedNotifications)
                .totalSmsUsed(totalSms)
                .totalWhatsappUsed(totalWhatsapp)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<BusinessResponse> getAllBusinesses(Pageable pageable) {
        return businessRepository.findAll(pageable).map(businessMapper::toResponse);
    }

    @Transactional
    public BusinessResponse toggleBusinessStatus(Long id, boolean active) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entreprise", "id", id));
        business.setActive(active);
        return businessMapper.toResponse(businessRepository.save(business));
    }
}
