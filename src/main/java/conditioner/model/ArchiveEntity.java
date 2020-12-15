package conditioner.model;

import conditioner.enums.TypeMaintenanceStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity(name = "Archive")
@Table(name="archive")
public class ArchiveEntity extends BaseEntity implements Serializable  {


    @Column(nullable = false, length = 100)
    private String nameConditioner;

    @Column(nullable = false, length = 100)
    private String inventoryNumber;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String typeMaintenanceName;

    @Column(nullable = false)
    private String firstWorkerUuid;

    @Column(nullable = false)
    private String secondWorkerUuid;

    /**
     * когда начали работу
     */
    @Column(nullable = false)
    private LocalDateTime startWorkTime;
    /**
     * когда закончили работу
     */
    @Column(nullable = false)
    private LocalDateTime finishWorkTime;
    /**
     * сколько часов работали
     */
    @Column(nullable = false)
    private Integer workedHours;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMaintenanceStatus status;

    @Column(nullable = false)
    private String description;
}
