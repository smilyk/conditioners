package conditioner.controller;
import conditioner.service.ConditionerServiceImpl;
import conditioner.dto.ConditionerDto;
import conditioner.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/conditioner")
public class ConditionerController {
    @Autowired
    ConditionerServiceImpl conditionerService;
    @Autowired
    ValidationService validationService;
    
    @PostMapping()
    public ConditionerDto createConditioner(@Valid  @RequestBody ConditionerDto conditionerDto) {
        validationService.validUniqConditioner(conditionerDto.getInventoryNumber());
        validationService.validConditioner(conditionerDto);
        return conditionerService.createConditioner(conditionerDto);
    }
    
    @GetMapping("/{conditionerUuid}")
    public ConditionerDto getConditioner(@PathVariable String conditionerUuid){
        return conditionerService.getConditionerById(conditionerUuid);
    }

    @GetMapping()
    public List<ConditionerDto> getAllConditioners(){
        return conditionerService.getAllConditioners();
    }
    @DeleteMapping("/{conditionerUuid}")
    public ConditionerDto deleteConditioner(@PathVariable String conditionerUuid){
        return conditionerService.deleteConditionerById(conditionerUuid);
    }

}
