package conditioner.dto;

import conditioner.model.InWorkEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlannedTypeMaintenanceDto {

    private String inventoryNumber;
    private String nameConditioner;
    private LocalDateTime startTime;
    private List<WorkerDto> workers;
    private String place;
    private TypeMaintenanceForDto typeMaintenance;
    private String planningRecordUuid;


}
