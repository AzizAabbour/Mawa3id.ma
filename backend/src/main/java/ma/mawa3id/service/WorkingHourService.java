package ma.mawa3id.service;

import lombok.RequiredArgsConstructor;
import ma.mawa3id.domain.entity.WorkingHour;
import ma.mawa3id.dto.workinghour.WorkingHourDto;
import ma.mawa3id.mapper.WorkingHourMapper;
import ma.mawa3id.repository.WorkingHourRepository;
import ma.mawa3id.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkingHourService {

    private final WorkingHourRepository workingHourRepository;
    private final WorkingHourMapper workingHourMapper;

    @Transactional(readOnly = true)
    public List<WorkingHourDto> getWorkingHours(Long employeeId) {
        Long businessId = TenantContext.getCurrentBusinessId();
        List<WorkingHour> hours;
        if (employeeId != null) {
            hours = workingHourRepository.findByBusinessIdAndEmployeeId(businessId, employeeId);
        } else {
            hours = workingHourRepository.findByBusinessIdAndEmployeeIdIsNull(businessId);
        }

        if (hours.isEmpty()) {
            // Return default Moroccan business schedule: Mon-Sat 09:00 - 20:00, Sun Off
            return createDefaultWorkingHours(businessId, employeeId);
        }

        return workingHourMapper.toDtoList(hours);
    }

    @Transactional
    public List<WorkingHourDto> saveWorkingHours(List<WorkingHourDto> dtoList) {
        Long businessId = TenantContext.getCurrentBusinessId();
        List<WorkingHour> result = new ArrayList<>();

        for (WorkingHourDto dto : dtoList) {
            WorkingHour entity;
            if (dto.getEmployeeId() != null) {
                entity = workingHourRepository.findByBusinessIdAndEmployeeIdAndDayOfWeek(
                        businessId, dto.getEmployeeId(), dto.getDayOfWeek())
                        .orElseGet(() -> WorkingHour.builder()
                                .businessId(businessId)
                                .employeeId(dto.getEmployeeId())
                                .dayOfWeek(dto.getDayOfWeek())
                                .build());
            } else {
                entity = workingHourRepository.findByBusinessIdAndEmployeeIdIsNullAndDayOfWeek(
                        businessId, dto.getDayOfWeek())
                        .orElseGet(() -> WorkingHour.builder()
                                .businessId(businessId)
                                .dayOfWeek(dto.getDayOfWeek())
                                .build());
            }

            entity.setStartTime(dto.getStartTime());
            entity.setEndTime(dto.getEndTime());
            entity.setBreakStartTime(dto.getBreakStartTime());
            entity.setBreakEndTime(dto.getBreakEndTime());
            entity.setDayOff(dto.isDayOff());

            result.add(workingHourRepository.save(entity));
        }

        return workingHourMapper.toDtoList(result);
    }

    private List<WorkingHourDto> createDefaultWorkingHours(Long businessId, Long employeeId) {
        List<WorkingHourDto> defaults = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            boolean isSunday = day == DayOfWeek.SUNDAY;
            defaults.add(WorkingHourDto.builder()
                    .employeeId(employeeId)
                    .dayOfWeek(day)
                    .startTime(LocalTime.of(9, 0))
                    .endTime(LocalTime.of(20, 0))
                    .breakStartTime(LocalTime.of(13, 0))
                    .breakEndTime(LocalTime.of(14, 0))
                    .dayOff(isSunday)
                    .build());
        }
        return defaults;
    }
}
