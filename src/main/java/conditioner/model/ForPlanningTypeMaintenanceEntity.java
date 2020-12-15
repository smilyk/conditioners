package conditioner.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
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
