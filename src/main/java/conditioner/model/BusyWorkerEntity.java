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
@Entity(name = "BusyWorkers")
@Table(name="busyWorkers")
public class BusyWorkerEntity extends BaseEntity implements Serializable {

    @Column(nullable = false, length = 100)
    private String workerUuid;

    @Column(nullable = false, length = 100)
    private String uuid;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime finishTime;


}
