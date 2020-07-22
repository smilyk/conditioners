package conditioner.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name="Type_maintenance")
@Table(name="type_maintenance")
public class TypeMaintenanceEntity extends BaseEntity implements Serializable {

    @Column(nullable = false, length = 50)
    private String uuidTypeMaintenance;

    @Column(nullable = false, length = 100)
    private String nameMaintenance;

    @Column(nullable = false)
    private String peopleHours;

    @Column
    @ManyToMany(mappedBy = "maintenance")
    private List<ConditionerEntity> conditioners = new ArrayList<>();
}
