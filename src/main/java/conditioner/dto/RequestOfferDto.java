package conditioner.dto;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RequestOfferDto {
    String client;
    List<RequestGetPriceDto> model;
}
