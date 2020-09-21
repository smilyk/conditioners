package conditioner.controller;

import conditioner.dto.DatesForPlanningDto;
import conditioner.dto.PlannedTypeMaintenanceDto;
import conditioner.dto.PlanningTypeMaintenanceConditioner;
import conditioner.model.ForPlanningTypeMaintenanceEntity;
import conditioner.service.PlanedService;
import conditioner.service.PlanningService;
import conditioner.service.ValidationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/planning")
@Api(value = "Planning Types Maintenance", description = "methods that collect data about the types maintenance ")

public class PlanningTypeMaintenanceController{

    @Autowired
    PlanningService planningService;

    @Autowired
    ValidationService validationService;

    @Autowired
    PlanedService planedService;

    @ApiOperation(value = "Getting all conditioners with missed date of type maintenance")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get all conditioners with missed date of type maintenance"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/missed")
    public List<PlanningTypeMaintenanceConditioner> getAllConditionerWithMissedTypeMaintenance(){
        return planningService.getAllConditionerWithMissedTypeMaintenance();
    }

    @ApiOperation(value = "Get  all conditioners for planning type maintenance")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get all all conditioners for planning type maintenance"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping()
    public List<PlanningTypeMaintenanceConditioner> getAllForPlanning(@RequestBody DatesForPlanningDto dates){
        /**
         * startDate == null ->  запрос на следующие Т дней (до конечной даты)
         * startDate and finishDate == null -> запрос на сегодня
         */
        if(dates.getStartDate() == null){
            dates.setStartDate(LocalDateTime.now().toLocalDate().atStartOfDay());
        }
        if (dates.getFinishDate() == null) {
            dates.setFinishDate(LocalDateTime.now().toLocalDate().atTime(23,59,59));
        }
        if(!dates.getStartDate().equals(dates.getFinishDate())){
            validationService.checkDatesForPlanning(dates);
        }
        return planningService.getAllForPlanning(dates);
    }

    @ApiOperation(value = "get plan conditioners for type maintenance by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully g0t plan conditioners for type maintenance by id"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/plan/{recordUuid}")
    public PlanningTypeMaintenanceConditioner getPlannedTypeMaintenanceRecordById(@PathVariable String recordUuid){
        return planedService.getPlannedTypeMaintenanceRecordById(recordUuid);
    }

    @ApiOperation(value = "to plan conditioners for type maintenance")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully planned conditioners for type maintenance"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/plan")
    public String plannedTypeMaintenance(@RequestBody PlannedTypeMaintenanceDto
                                                                        plannedTypeMaintenanceDto){
        return planedService.toPlanTypeMaintenance(plannedTypeMaintenanceDto);
    }

}
