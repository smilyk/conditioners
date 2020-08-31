package conditioner.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestForBusyWorkersDto {

    private LocalDateTime startDate;
    @NotNull(message = "field uuidTypeMaintenance id required")
    private String uuidTypeMaintenance;
    @NotNull(message = "field countOfWorkers id required")
    private Integer countOfWorkers;
}
