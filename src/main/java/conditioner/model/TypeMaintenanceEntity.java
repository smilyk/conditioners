package conditioner.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity(name="Type_maintenance")
@Table(name="type_maintenance")
public class TypeMaintenanceEntity extends BaseEntity implements Serializable {

    @Column(nullable = false, length = 50)
    private String uuidTypeMaintenance;

    @Column(nullable = false, length = 100)
    private String nameMaintenance;

    @Column(nullable = false)
    private Integer peopleHours;

    @Column(nullable = false)
    private Boolean deleted = false;

    /**
     *  сколько часов до следующего ТО
     */
    @Column(nullable = false)
    private Integer hoursBeforeTypeMaintenance;

    @Column
    @ManyToMany(mappedBy = "maintenance", fetch = FetchType.EAGER)
    private List<ConditionerEntity> conditioners = new ArrayList<>();
}
