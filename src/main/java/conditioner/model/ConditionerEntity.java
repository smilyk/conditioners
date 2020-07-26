package conditioner.model;

import lombok.*;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
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
    private Date startDate;

    @Column
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
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
