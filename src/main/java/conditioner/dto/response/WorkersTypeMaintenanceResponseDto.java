package conditioner.dto.response;

import conditioner.dto.TypeMaintenanceForDto;
import conditioner.dto.WorkerDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 *  ТО, которые получает работник, когда хочет посмотреть информацио о ТО
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class WorkersTypeMaintenanceResponseDto {

    String planningRecordsUuid;
    String nameConditioner;
    String inventoryNumber;
    String place;
    String typeMaintenanceName;
    Integer typeMaintenancePeopleHorse;
    LocalDateTime startTimes;
    List<WorkerDto> workers;
}
