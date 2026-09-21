package ma.mawa3id.mapper;

import ma.mawa3id.domain.entity.WorkingHour;
import ma.mawa3id.dto.workinghour.WorkingHourDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkingHourMapper {
    WorkingHourDto toDto(WorkingHour entity);
    List<WorkingHourDto> toDtoList(List<WorkingHour> entities);
    WorkingHour toEntity(WorkingHourDto dto);
}
