package conditioner.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity(name = "Sell")
@Table(name="sell")
public class PriceEntity extends BaseEntity implements Serializable {
    @Column(nullable = false, length = 50)
    private String uuidPosition;
//название
    @Column(nullable = false, length = 100)
    private String namePosition;
//модель
    @Column(nullable = false, length = 100)
    private String modelPosition;
//закупка укр
    @Column(nullable = false)
    private Double priceUkr;
//закупка сша
    @Column(nullable = false)
    private Double priceUsa;
//цена на рынке
    @Column(nullable = false)
    private Double priceMarketPosition;
//работа (монтаж)
    @Column(nullable = false)
    private Double workPricePosition;
//коэф
    @Column(nullable = false)
    private Double coefficientPosition;

//    единицы измерения
    @Column(nullable = false, length = 100)
    private String unitsPosition;
//описание
    @Column(nullable = false, length = 100)
    private String descriptionPosition;


}
