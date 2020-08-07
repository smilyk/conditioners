package conditioner.dto;

import conditioner.model.TypeMaintenanceEntity;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlanningTypeMaintenanceConditioner {
    String uuidConditioner;
    String nameConditioner;
    String inventoryNumber;
    String place;
    TypeMaintenanceForDto maintenance;
    LocalDateTime lastTypeMaintenanceDate;
    LocalDateTime nextTypeMaintenanceDate;

}
