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
    private Integer QRangeMin;
    @Value("${QRangeMax}")
    private Integer QRangeMax;
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
        Double computerCountTmp = calculatorDto.getK() * computerCount;
        Double tvCountTmp = calculatorDto.getT() * tvCount;
        Double othersCountTmp = calculatorDto.getA() * othersCount;
        Double q3 = computerCountTmp + tvCountTmp + othersCountTmp;
        Double query = q1 + q2 + q3;
        Double maxProc = QRangeMax * 0.1;
        Double minProc = QRangeMin * 0.1;
        Double queryMin = query*minProc;
        Double queryMax = query*maxProc;
        return CalculatorResponseDto.builder()
                .max(query - queryMax)
                .min(query + queryMin).
                        build();

    }
}
