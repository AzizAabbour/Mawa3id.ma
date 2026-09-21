package ma.mawa3id.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Working hours per day of week for a business or specific employee.
 */
@Entity
@Table(name = "working_hours", indexes = {
        @Index(name = "idx_working_hours_business_id", columnList = "business_id"),
        @Index(name = "idx_working_hours_employee_id", columnList = "employee_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkingHour extends BaseEntity {

    @Column(name = "business_id", nullable = false)
    private Long businessId;

    @Column(name = "employee_id")
    private Long employeeId; // Nullable: applies to whole business if null

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 15)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "break_start_time")
    private LocalTime breakStartTime;

    @Column(name = "break_end_time")
    private LocalTime breakEndTime;

    @Column(name = "is_day_off", nullable = false)
    @Builder.Default
    private boolean dayOff = false;
}
