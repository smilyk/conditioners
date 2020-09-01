package conditioner.controller;

import conditioner.dto.DatesForPlanningDto;
import conditioner.dto.WorkersTypeMaintenanceResponseDto;
import conditioner.service.ValidationService;
import conditioner.service.WorkersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/workers")
@Api(value = "Workers", description = "methods that collect data about the workers ")
public class WorkersController {

    @Autowired
    WorkersService workersService;
    @Autowired
    ValidationService validationService;


    @ApiOperation(value = "Getting a conditioner with missed date of Type Maintenance")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get a conditioner with missed date of Type Maintenance"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
   @GetMapping("/missed/{workerUuid}")
   public List<WorkersTypeMaintenanceResponseDto> getAllConditionerWithMissedTypeMaintenance(
           @PathVariable String workerUuid){
       validationService.checkWorker(workerUuid);
       return workersService.getAllConditionerWithMissedTypeMaintenance(workerUuid);
   }

    @ApiOperation(value = "Getting all planned records for work")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get all planned records for work"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping("/{workerUuid}")
    public List<WorkersTypeMaintenanceResponseDto> getAllRecordsForWork(@RequestBody DatesForPlanningDto dates,
                                                                        @PathVariable String workerUuid){
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
        return workersService.getAllRecordsForWork(dates, workerUuid);
    }

    @ApiOperation(value = "Take conditioner 'to work'")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully took conditioner 'to work'"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PutMapping("/{recordUuid}/{workerUuid}")
    public WorkersTypeMaintenanceResponseDto makeTypeMaintenanceInWork(@PathVariable String recordUuid,
                                                                        @PathVariable String workerUuid){
        /**
         * проверка - может ли этот работник брать в работу эту запись
         */
       validationService.checkWorkerAndRecord(recordUuid, workerUuid);
       return workersService.getTypeMaintenanceInWork(recordUuid, workerUuid);
    }

    @ApiOperation(value = "Finishing of working with conditioner")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully finished  work with conditioner"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PutMapping("/done/{recordUuid}/{workerUuid}")
    public WorkersTypeMaintenanceResponseDto makeTypeMaintenanceInDone(@PathVariable String recordUuid,
                                                                       @PathVariable String workerUuid){
        /**
         * проверка - может ли этот работник брать в работу эту запись
         */
        validationService.checkWorkerAndRecord(recordUuid, workerUuid);
        return workersService.getTypeMaintenanceDone(recordUuid, workerUuid);
    }

}
