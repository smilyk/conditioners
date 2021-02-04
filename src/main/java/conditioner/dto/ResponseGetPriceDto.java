package conditioner.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ResponseGetPriceDto {
    String name;
    String model;
    Double count;
    //    цена по прайсу Укр
    Double priceDefaultUkr;
    //    ощбая стоисоть Укр
    Double priceGlobalUkr;
    //    прибыль гривна
    Double profitUkr;
    //     цена Укр
    Double priceUkr;
    //    рентабельность Украина
    Double profitabilityUkr;
    //    цена по прайсу Usa
    Double priceDefaultUsa;
    //    ощбая стоисоть Usa
    Double priceGlobalUsa;
    //    прибыль Usa
    Double profitUsa;
    //     цена Usa
    Double priceUsa;
    //    рентабельность Usa
    Double profitabilityUsa;


}
