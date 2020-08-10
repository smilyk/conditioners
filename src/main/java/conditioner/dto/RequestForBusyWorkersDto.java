package conditioner.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestForBusyWorkersDto {
    private LocalDateTime startDate;
    private String uuidTypeMaintenance;
    private Integer countOfWorkers;
}
