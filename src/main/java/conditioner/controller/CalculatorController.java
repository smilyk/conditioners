package conditioner.controller;

import conditioner.dto.ArticleDto;
import conditioner.dto.CalculatorDto;
import conditioner.dto.CalculatorResponseDto;
import conditioner.service.CalculatorServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/calculator")
@Api(value = "Calculator", description = "methods that collect data about the calculator ")
public class CalculatorController {

 @Autowired
 CalculatorServiceImpl calculatorService;
    @ApiOperation(value="Get power of conditioner")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping
    public CalculatorResponseDto getPower(@Valid @RequestBody CalculatorDto calculatorDto){
        return calculatorService.getRecomendationPower(calculatorDto);
    }
}
