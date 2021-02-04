package conditioner.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity(name = "Offer")
@Table(name="offer")
public class OfferEntity extends BaseEntity implements Serializable  {

    @Column(nullable = false, length = 50)
    private String uuidOffer;

    @Column(nullable = false, length = 100)
    private String nameModel;

    @Column(nullable = false)
    private Double priceUkr;

    @Column(nullable = false)
    private Double priceUsa;

    @Column(nullable = false)
    private Double priceWork;

    @Column(nullable = false)
    private Double sum;

    @Column(nullable = false)
    private Double priceInternet;

    @Column(nullable = false)
    private String client;

}
