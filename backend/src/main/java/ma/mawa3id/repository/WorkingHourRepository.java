package ma.mawa3id.repository;

import ma.mawa3id.domain.entity.WorkingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkingHourRepository extends JpaRepository<WorkingHour, Long> {
    List<WorkingHour> findByBusinessId(Long businessId);
    List<WorkingHour> findByBusinessIdAndEmployeeIdIsNull(Long businessId);
    List<WorkingHour> findByBusinessIdAndEmployeeId(Long businessId, Long employeeId);
    Optional<WorkingHour> findByBusinessIdAndEmployeeIdIsNullAndDayOfWeek(Long businessId, DayOfWeek dayOfWeek);
    Optional<WorkingHour> findByBusinessIdAndEmployeeIdAndDayOfWeek(Long businessId, Long employeeId, DayOfWeek dayOfWeek);
}
