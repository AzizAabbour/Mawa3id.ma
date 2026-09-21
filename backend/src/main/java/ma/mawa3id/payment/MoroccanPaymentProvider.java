package ma.mawa3id.payment;

import lombok.extern.slf4j.Slf4j;
import ma.mawa3id.domain.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Moroccan Payment Gateway Implementation (Supports CMI / Payzone simulation and test mode).
 */
@Component
@Slf4j
public class MoroccanPaymentProvider implements PaymentProvider {

    @Override
    public String getProviderName() {
        return "CMI_MOROCCO";
    }

    @Override
    public PaymentResultDto processPayment(PaymentRequestDto request) {
        log.info("[PAYMENT] Initializing Moroccan payment of {} MAD for business ID {}",
                request.getAmountMad(), request.getBusinessId());

        String txnId = "CMI_" + UUID.randomUUID().toString().substring(0, 16).toUpperCase();

        return PaymentResultDto.builder()
                .success(true)
                .transactionId(txnId)
                .status(PaymentStatus.COMPLETED)
                .message("Paiement simulé validé avec succès (Mode Test Maroc)")
                .build();
    }

    @Override
    public PaymentResultDto verifyPayment(String transactionId) {
        log.info("[PAYMENT] Verifying Moroccan transaction: {}", transactionId);
        return PaymentResultDto.builder()
                .success(true)
                .transactionId(transactionId)
                .status(PaymentStatus.COMPLETED)
                .message("Paiement vérifié avec succès")
                .build();
    }
}
