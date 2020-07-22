package conditioner.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import conditioner.service.ConditionerServiceImpl;
import conditioner.dto.ConditionerDto;
import conditioner.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}
