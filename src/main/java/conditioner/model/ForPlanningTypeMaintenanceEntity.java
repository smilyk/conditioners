package conditioner.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "ForPlanning")
@Table(name="forPlanning")

public class ForPlanningTypeMaintenanceEntity extends BaseEntity implements Serializable {
    @Column(nullable = false, length = 50)
    private String uuidRecord;

    @Column(nullable = false, length = 50)
    private String uuidConditioner;

    @Column(nullable = false, length = 50)
    private String uuidTypeMaintenance;

    @Column(nullable = false, length = 100)
    private String nameConditioner;

    @Column(nullable = false, length = 100)
    private String inventoryNumber;

    @Column(nullable = false)
    private String place;

    @Column(nullable = true)
    private LocalDateTime lastTypeMaintenanceDate;

    @Column(nullable = true)
    private LocalDateTime nextTypeMaintenanceDate;
}
