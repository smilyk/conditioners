package conditioner.dto;


import conditioner.model.TypeMaintenanceEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ConditionerDto {

    private String uuidConditioner;
    @NotNull(message = "field nameConditioner id required")
    private String nameConditioner;
    @NotNull(message = "field inventoryNumber id required")
    private String inventoryNumber;
    @NotNull(message = "field place id required")
    private String place;
    private LocalDateTime startDate;
    private List<TypeMaintenanceForDto> maintenance = new ArrayList<>();
    /**
     * сколько часов кондиуионер должен отработать до того, как необходимо
     * провести ТО
     *
     * в случае остановки кондиционера, сохраняется количество часов,
     * которые он успел отработать с момента последнего ТО
     */
    @NotNull(message = "field workedHours id required")
    private Integer workedHours;
    @NotNull(message = "field deleted id required")
    private Boolean deleted;

}
