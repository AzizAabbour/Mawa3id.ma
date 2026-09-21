package ma.mawa3id.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanResponse {
    private Long id;
    private UUID publicId;
    private String name;
    private String slug;
    private String description;
    private BigDecimal priceMonthlyMad;
    private Integer maxAppointmentsPerMonth;
    private Integer maxCustomers;
    private Integer maxEmployees;
    private boolean whatsappNotificationsIncluded;
    private boolean smsNotificationsIncluded;
    private List<String> features;
    private boolean active;
    private Integer displayOrder;
}
