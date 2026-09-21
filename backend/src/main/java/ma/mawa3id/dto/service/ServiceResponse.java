package ma.mawa3id.dto.service;

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
public class ServiceResponse {
    private Long id;
    private UUID publicId;
    private String name;
    private String description;
    private Integer durationMinutes;
    private BigDecimal priceMad;
    private String color;
    private boolean active;
    private List<ServiceEmployeeDto> assignedEmployees;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceEmployeeDto {
        private Long id;
        private UUID publicId;
        private String fullName;
        private String title;
    }
}
