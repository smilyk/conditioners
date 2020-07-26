package conditioner.dto;

import conditioner.model.ConditionerEntity;
import lombok.*;


import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TypeMaintenanceDto {


    private String uuidTypeMaintenance;

    @NotNull(message = "field nameMaintenance id required")
    private String nameMaintenance;

    @NotNull(message = "field peopleHours id required")
    private String peopleHours;

    @NotNull(message = "field deleted id required")
    private Boolean deleted = false;

//    private List<ConditionerForDto> conditioners = new ArrayList<>();
}
