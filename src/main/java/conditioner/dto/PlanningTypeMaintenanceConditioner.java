package conditioner.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlanningTypeMaintenanceConditioner {
    String uuidRecords;
    String uuidConditioner;
    String nameConditioner;
    String inventoryNumber;
    String place;
    TypeMaintenanceForDto maintenance;
    LocalDateTime lastTypeMaintenanceDate;
    LocalDateTime nextTypeMaintenanceDate;

}
