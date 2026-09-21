package ma.mawa3id.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.domain.enums.SubscriptionStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {
    private Long id;
    private UUID publicId;
    private SubscriptionPlanResponse plan;
    private SubscriptionStatus status;
    private LocalDateTime currentPeriodStart;
    private LocalDateTime currentPeriodEnd;
    private LocalDateTime trialEndsAt;
    private boolean active;
    private long currentMonthAppointmentsCount;
    private long totalCustomersCount;
    private long totalEmployeesCount;
}
