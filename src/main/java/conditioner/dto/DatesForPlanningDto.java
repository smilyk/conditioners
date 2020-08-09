package conditioner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DatesForPlanningDto {
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
}
