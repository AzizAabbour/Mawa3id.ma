package ma.mawa3id.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.domain.enums.PaymentStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResultDto {
    private boolean success;
    private String transactionId;
    private String redirectUrl;
    private PaymentStatus status;
    private String message;
}
