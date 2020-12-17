package conditioner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import conditioner.dto.CalculatorDto;
import conditioner.dto.CalculatorResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CalculatorServiceImpl {

    private static final Logger LOGGER = LogManager.getLogger(ConditionerServiceImpl.class);

    private ObjectMapper mapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();


    @Value("${QRangeMin}")
    private Double QRangeMin;
    @Value("${QRangeMax}")
    private Double QRangeMax;
    @Value("${peopleCount}")
    private Double peopleCount;
    @Value("${computerCount}")
    private Double computerCount;
    @Value("${tvCount}")
    private Double tvCount;
    @Value("${othersCount}")
    private Double othersCount;

    public CalculatorResponseDto getRecomendationPower(CalculatorDto calculatorDto) {
        Double tmp = calculatorDto.getH()*calculatorDto.getS()*Integer.parseInt(calculatorDto.getQ());
        Double q1 = tmp/1000;
        Double q2 = calculatorDto.getN()*peopleCount;
        Double q3 = (calculatorDto.getK() * computerCount) +
                (calculatorDto.getT() * tvCount)+
                (calculatorDto.getA() * othersCount);
        Double query = q1 + q2 + q3;
        Double queryMin = query*((100-QRangeMin)/100);
        Double queryMax = query*((100+QRangeMax)/100);
        return CalculatorResponseDto.builder()
                .max(queryMax)
                .min(queryMin)
                        .build();
    }
}
