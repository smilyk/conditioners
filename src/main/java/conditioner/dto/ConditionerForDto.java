package conditioner.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ConditionerForDto {
    private String uuidConditioner;
    private String nameConditioner;
    private String inventoryNumber;
    private String place;
    private Date startDate;
}