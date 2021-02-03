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

    @Column(nullable = false, length = 100)
    private String namePosition;

    @Column(nullable = false, length = 100)
    private String modelPosition;

    @Column(nullable = false)
    private Double priceUkr;

    @Column(nullable = false)
    private Double priceUsa;

    @Column(nullable = false)
    private Double priceMarketPosition;

    @Column(nullable = false)
    private Double workPricePosition;

    @Column(nullable = false)
    private Double coefficientPosition;

//    единицы измерения
    @Column(nullable = false, length = 100)
    private String unitsPosition;

    @Column(nullable = false, length = 100)
    private String descriptionPosition;


}
