package ma.mawa3id.service;

import lombok.RequiredArgsConstructor;
import ma.mawa3id.domain.entity.Payment;
import ma.mawa3id.domain.entity.Subscription;
import ma.mawa3id.domain.entity.SubscriptionPlan;
import ma.mawa3id.domain.enums.PaymentStatus;
import ma.mawa3id.domain.enums.SubscriptionStatus;
import ma.mawa3id.dto.subscription.SubscriptionPlanResponse;
import ma.mawa3id.dto.subscription.SubscriptionResponse;
import ma.mawa3id.exception.ResourceNotFoundException;
import ma.mawa3id.mapper.SubscriptionPlanMapper;
import ma.mawa3id.payment.MoroccanPaymentProvider;
import ma.mawa3id.payment.PaymentRequestDto;
import ma.mawa3id.payment.PaymentResultDto;
import ma.mawa3id.repository.*;
import ma.mawa3id.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository planRepository;
    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final SubscriptionPlanMapper planMapper;
    private final MoroccanPaymentProvider paymentProvider;

    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponse> getPlans() {
        return planMapper.toResponseList(planRepository.findByActiveTrueOrderByDisplayOrderAsc());
    }

    @Transactional(readOnly = true)
    public SubscriptionResponse getCurrentSubscription() {
        Long businessId = TenantContext.getCurrentBusinessId();
        Subscription sub = subscriptionRepository.findByBusinessId(businessId)
                .orElseThrow(() -> new ResourceNotFoundException("Abonnement introuvable pour cette entreprise"));

        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        long appointmentsThisMonth = appointmentRepository.countByBusinessIdAndCreatedAtAfter(businessId, startOfMonth);
        long customersCount = customerRepository.countByBusinessId(businessId);
        long employeesCount = employeeRepository.countByBusinessIdAndActiveTrue(businessId);

        return SubscriptionResponse.builder()
                .id(sub.getId())
                .publicId(sub.getPublicId())
                .plan(planMapper.toResponse(sub.getPlan()))
                .status(sub.getStatus())
                .currentPeriodStart(sub.getCurrentPeriodStart())
                .currentPeriodEnd(sub.getCurrentPeriodEnd())
                .trialEndsAt(sub.getTrialEndsAt())
                .active(sub.isActive())
                .currentMonthAppointmentsCount(appointmentsThisMonth)
                .totalCustomersCount(customersCount)
                .totalEmployeesCount(employeesCount)
                .build();
    }

    @Transactional
    public SubscriptionResponse upgradeSubscription(String planSlug, String paymentMethod) {
        Long businessId = TenantContext.getCurrentBusinessId();
        SubscriptionPlan plan = planRepository.findBySlug(planSlug)
                .orElseThrow(() -> new ResourceNotFoundException("Forfait", "slug", planSlug));

        Subscription sub = subscriptionRepository.findByBusinessId(businessId)
                .orElseGet(() -> Subscription.builder().businessId(businessId).build());

        // Process Moroccan payment in MAD
        PaymentRequestDto paymentRequest = PaymentRequestDto.builder()
                .businessId(businessId)
                .subscriptionId(sub.getId())
                .amountMad(plan.getPriceMonthlyMad())
                .paymentMethod(paymentMethod != null ? paymentMethod : "CARD")
                .build();

        PaymentResultDto paymentResult = paymentProvider.processPayment(paymentRequest);

        // Record payment
        Payment payment = Payment.builder()
                .businessId(businessId)
                .subscriptionId(sub.getId())
                .amountMad(plan.getPriceMonthlyMad())
                .currency("MAD")
                .status(PaymentStatus.COMPLETED)
                .providerName(paymentProvider.getProviderName())
                .providerTransactionId(paymentResult.getTransactionId())
                .paymentMethod(paymentMethod != null ? paymentMethod : "CARD")
                .paidAt(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        // Update subscription
        sub.setPlan(plan);
        sub.setStatus(SubscriptionStatus.ACTIVE);
        sub.setCurrentPeriodStart(LocalDateTime.now());
        sub.setCurrentPeriodEnd(LocalDateTime.now().plusMonths(1));
        sub = subscriptionRepository.save(sub);

        return getCurrentSubscription();
    }
}
