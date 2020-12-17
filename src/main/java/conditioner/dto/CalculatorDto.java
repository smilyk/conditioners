package conditioner.dto;


import lombok.*;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CalculatorDto {


    @NotNull(message = "field articleName id required")
    private Integer s;
    @NotNull(message = "field articleName id required")
    private Double h = 1.0;
    private String q = "3";
    private Integer n = 0;
    private Integer k = 0;
    private Integer t = 0;
    private Double a = 0.0;
    private String uuidArticle;
}
