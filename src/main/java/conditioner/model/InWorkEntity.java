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
@Entity(name = "InWork")
@Table(name="inWork")
public class InWorkEntity extends BaseEntity implements Serializable {

    @Column(nullable = false, length = 50)
    public String recordsUuid;

    @Column(nullable = false, length = 100)
    private String inventoryNumber;

    @Column(nullable = false, length = 100)
    private String typeMaintenanceUuid;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime finishTime;

    @Column(nullable = false, length = 100)
    private String firstWorkerUuid;

    @Column(nullable = false, length = 100)
    private String secondWorkerUuid;

    @Column(nullable = false)
    private Boolean in_work;

    @Column(nullable = false)
    private Boolean done = false;

}
