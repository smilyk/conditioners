package conditioner.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CalculatorResponseDto {

    Double min;
    Double max;
    Double recom;

}
