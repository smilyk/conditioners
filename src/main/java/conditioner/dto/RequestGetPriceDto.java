package conditioner.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RequestGetPriceDto {
    String name;
    String model;
    Double count;
}
