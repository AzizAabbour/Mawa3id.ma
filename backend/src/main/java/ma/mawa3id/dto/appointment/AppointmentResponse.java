package ma.mawa3id.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.mawa3id.domain.enums.AppointmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private UUID publicId;
    private Long businessId;

    // Customer info
    private Long customerId;
    private UUID customerPublicId;
    private String customerName;
    private String customerPhone;
    private String customerWhatsapp;
    private String customerEmail;

    // Service info
    private Long serviceId;
    private UUID servicePublicId;
    private String serviceName;
    private Integer serviceDurationMinutes;
    private BigDecimal servicePriceMad;
    private String serviceColor;

    // Employee info
    private Long employeeId;
    private UUID employeePublicId;
    private String employeeName;

    // Schedule & status
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentStatus status;
    private String notes;
    private String cancellationReason;
    private LocalDateTime cancelledAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
}
