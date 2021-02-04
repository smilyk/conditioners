package conditioner.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OfferPriceDto {
    String model;
    Double priceUkr;
    Double priceUsa;
    Double workPriceUkr;
    Double sumUkr;
    Double priceInternet;

}
