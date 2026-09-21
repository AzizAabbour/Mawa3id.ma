package ma.mawa3id.payment;

/**
 * Common abstraction for Moroccan payment providers (CMI, Payzone, CashPlus, etc.).
 */
public interface PaymentProvider {
    String getProviderName();
    PaymentResultDto processPayment(PaymentRequestDto request);
    PaymentResultDto verifyPayment(String transactionId);
}
