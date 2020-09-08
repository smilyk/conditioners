package conditioner.controller;

import conditioner.dto.ConditionerDto;
import conditioner.service.ConditionerServiceImpl;
import conditioner.service.ValidationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/conditioner")
@Api(value = "Conditioners", description = "methods that collect data about the conditioners ")
public class ConditionerController {
    @Autowired
    ConditionerServiceImpl conditionerService;
    @Autowired
    ValidationService validationService;

    @ApiOperation(value = "Adding a new conditioner to DataBase")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added cupboard"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )

    @PostMapping()
    public ConditionerDto createConditioner(@Valid  @RequestBody ConditionerDto conditionerDto) {
        validationService.validUniqConditioner(conditionerDto.getInventoryNumber());
        validationService.validConditioner(conditionerDto);
        return conditionerService.createConditioner(conditionerDto);
    }

    @ApiOperation(value = "Getting a conditioner from DB")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get conditioner"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/{conditionerUuid}")
    public ConditionerDto getConditioner(@PathVariable String conditionerUuid){
        return conditionerService.getConditionerById(conditionerUuid);
    }

    @ApiOperation(value = "Getting a conditioner from DB by inventoryNumber")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get conditioner"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/{conditionerInventoryNumber}")
    public ConditionerDto getConditionerByInventoryNumber(@PathVariable String conditionerInventoryNumber){
        return conditionerService.getConditionerByInventoryNumber(conditionerInventoryNumber);
    }

    @ApiOperation(value = "Getting all conditioners from DB")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get all conditioners"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )

    @GetMapping()
    public List<ConditionerDto> getAllConditioners(){
        return conditionerService.getAllConditioners();
    }
    @DeleteMapping("/{conditionerUuid}")
    public ConditionerDto deleteConditioner(@PathVariable String conditionerUuid){
        return conditionerService.deleteConditionerById(conditionerUuid);
    }

    @ApiOperation(value = "To start conditioner")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "To start conditione"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PutMapping("/start/{conditionerUuid}")
    public String startWorkConditioner(@PathVariable String conditionerUuid){
        return conditionerService.startWorkConditioner(conditionerUuid);
    }


    @ApiOperation(value = "Getting all conditioners without Type Maintenance")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get all conditioners without Type Maintenance"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/not-type-maintenance")
    public List<ConditionerDto> getAllNotTypeMaintenanceConditioner(){
        return conditionerService.getAllNotTypeMaintenanceConditioners();
    }

    @ApiOperation(value = "put Type Maintenance to Conditioner")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully put Type Maintenance to Conditioner"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PutMapping("/{conditionerUuid}/{typeMaintenanceUuid}")
    public ConditionerDto putTypeMaintenanceToConditioner(@PathVariable String conditionerUuid,
                                                          @PathVariable String typeMaintenanceUuid){
        return conditionerService.addTypeMaintenanceToConditioner(conditionerUuid, typeMaintenanceUuid);
    }

    @ApiOperation(value = "get all not started conditioners")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get all not started conditioners"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/not-start")
    public List<ConditionerDto> getAllNotStartedConditioners(){
        return conditionerService.getAllNotStartedConditioner();
    }

}
