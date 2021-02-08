package conditioner.dto;

import lombok.*;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PriceDto {

    private String uuidPosition;
    //название
    private String namePosition;
    //модель
    private String modelPosition;
    //закупка укр
    private Double priceUkr;
    //закупка сша
    private Double priceUsa;
    //цена на рынке
    private Double priceMarketPosition;
    //работа (монтаж)
    private Double workPricePosition;
    //коэф
    private Double coefficientPosition;
    //единицы измерения
    private String unitsPosition;
    //описание
    private String descriptionPosition;

}
