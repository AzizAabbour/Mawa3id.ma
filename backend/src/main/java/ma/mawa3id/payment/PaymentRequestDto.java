package ma.mawa3id.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    private Long businessId;
    private Long subscriptionId;
    private BigDecimal amountMad;
    private String paymentMethod; // "CARD", "CASH_PLUS", "BANK_TRANSFER"
    private String returnUrl;
    private String cancelUrl;
}
