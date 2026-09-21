package ma.mawa3id.repository;

import ma.mawa3id.domain.entity.Appointment;
import ma.mawa3id.domain.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByPublicId(UUID publicId);
    Optional<Appointment> findByPublicIdAndBusinessId(UUID publicId, Long businessId);

    List<Appointment> findByBusinessIdAndAppointmentDate(Long businessId, LocalDate date);

    List<Appointment> findByBusinessIdAndAppointmentDateBetween(
            Long businessId, LocalDate startDate, LocalDate endDate);

    List<Appointment> findByEmployeeIdAndAppointmentDate(Long employeeId, LocalDate date);

    Page<Appointment> findByBusinessId(Long businessId, Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.businessId = :businessId AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:employeeId IS NULL OR a.employee.id = :employeeId) AND " +
           "(:startDate IS NULL OR a.appointmentDate >= :startDate) AND " +
           "(:endDate IS NULL OR a.appointmentDate <= :endDate)")
    Page<Appointment> findFiltered(
            @Param("businessId") Long businessId,
            @Param("status") AppointmentStatus status,
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    /**
     * Prevents double booking: checks for overlapping active appointments for the given employee on a specific date.
     */
    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.employee.id = :employeeId " +
           "AND a.appointmentDate = :date " +
           "AND a.status NOT IN ('CANCELLED', 'NO_SHOW') " +
           "AND (:excludeId IS NULL OR a.id != :excludeId) " +
           "AND ((a.startTime < :endTime AND a.endTime > :startTime))")
    boolean existsOverlapping(
            @Param("employeeId") Long employeeId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeId") Long excludeId);

    /**
     * Finds upcoming appointments for reminder scheduling.
     */
    @Query("SELECT a FROM Appointment a WHERE a.status = 'CONFIRMED' " +
           "AND a.appointmentDate = :date " +
           "AND a.startTime BETWEEN :fromTime AND :toTime")
    List<Appointment> findUpcomingForReminder(
            @Param("date") LocalDate date,
            @Param("fromTime") LocalTime fromTime,
            @Param("toTime") LocalTime toTime);

    long countByBusinessIdAndCreatedAtAfter(Long businessId, LocalDateTime dateTime);
    long countByBusinessIdAndStatus(Long businessId, AppointmentStatus status);
    long countByBusinessIdAndAppointmentDate(Long businessId, LocalDate date);
}
