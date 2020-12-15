package conditioner.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity(name = "Conditioner")
@Table(name="conditioner")
public class ConditionerEntity extends BaseEntity implements Serializable  {

    @Column(nullable = false, length = 50)
    private String uuidConditioner;

    @Column(nullable = false, length = 100)
    private String nameConditioner;

    @Column(nullable = false, unique = true, length = 100)
    private String inventoryNumber;

    @Column(nullable = false)
    private String place;

    @Column(nullable = true)
    private LocalDateTime startDate;

    @Column
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    }, fetch=FetchType.EAGER)
    @JoinTable(name = "conditioner_type_maintenance",
            joinColumns = @JoinColumn(name = "conditioner_id"),
            inverseJoinColumns = @JoinColumn(name = "type_maintenance_id")
    )
    private List<TypeMaintenanceEntity> maintenance = new ArrayList<>();

    private Boolean start;
    /**
     * в случае остановки кондиционера, сохраняется количество часов,
     * которые он успел отработать с момента последнего ТО
   */
    private Integer workedHours;
    @Column(nullable = false)
    private Boolean deleted = false;

}
