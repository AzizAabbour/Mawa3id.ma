package ma.mawa3id.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import ma.mawa3id.domain.entity.WorkingHour;
import ma.mawa3id.dto.workinghour.WorkingHourDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-21T16:14:26+0100",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.100.v20260826-1225, environment: Java 21.0.12.1 (Eclipse Adoptium)"
)
@Component
public class WorkingHourMapperImpl implements WorkingHourMapper {

    @Override
    public WorkingHourDto toDto(WorkingHour entity) {
        if ( entity == null ) {
            return null;
        }

        WorkingHourDto.WorkingHourDtoBuilder workingHourDto = WorkingHourDto.builder();

        workingHourDto.breakEndTime( entity.getBreakEndTime() );
        workingHourDto.breakStartTime( entity.getBreakStartTime() );
        workingHourDto.dayOfWeek( entity.getDayOfWeek() );
        workingHourDto.dayOff( entity.isDayOff() );
        workingHourDto.employeeId( entity.getEmployeeId() );
        workingHourDto.endTime( entity.getEndTime() );
        workingHourDto.id( entity.getId() );
        workingHourDto.startTime( entity.getStartTime() );

        return workingHourDto.build();
    }

    @Override
    public List<WorkingHourDto> toDtoList(List<WorkingHour> entities) {
        if ( entities == null ) {
            return null;
        }

        List<WorkingHourDto> list = new ArrayList<WorkingHourDto>( entities.size() );
        for ( WorkingHour workingHour : entities ) {
            list.add( toDto( workingHour ) );
        }

        return list;
    }

    @Override
    public WorkingHour toEntity(WorkingHourDto dto) {
        if ( dto == null ) {
            return null;
        }

        WorkingHour.WorkingHourBuilder workingHour = WorkingHour.builder();

        workingHour.breakEndTime( dto.getBreakEndTime() );
        workingHour.breakStartTime( dto.getBreakStartTime() );
        workingHour.dayOfWeek( dto.getDayOfWeek() );
        workingHour.dayOff( dto.isDayOff() );
        workingHour.employeeId( dto.getEmployeeId() );
        workingHour.endTime( dto.getEndTime() );
        workingHour.startTime( dto.getStartTime() );

        return workingHour.build();
    }
}
