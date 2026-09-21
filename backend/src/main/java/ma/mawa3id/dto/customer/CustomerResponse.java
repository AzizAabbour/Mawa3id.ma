package ma.mawa3id.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {
    private Long id;
    private UUID publicId;
    private String fullName;
    private String phone;
    private String whatsappNumber;
    private String email;
    private String notes;
    private Integer totalAppointments;
    private BigDecimal totalSpentMad;
    private LocalDateTime lastAppointmentAt;
    private LocalDateTime createdAt;
}
