package conditioner.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TypeMaintenanceForDto {
    private String uuidTypeMaintenance;

    private String nameMaintenance;

    private Integer peopleHours;

    private Boolean deleted = false;

    /**
     * сколько часов нужно ортработать до следущего ТО
     */
    private Integer hoursBeforeTypeMaintenance;

}
