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

    private String peopleHours;

    private Boolean deleted = false;

}
