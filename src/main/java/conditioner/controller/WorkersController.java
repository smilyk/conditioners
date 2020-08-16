package conditioner.controller;

import conditioner.dto.DatesForPlanningDto;
import conditioner.dto.PlanningTypeMaintenanceConditioner;
import conditioner.dto.response.WorkersTypeMaintenanceResponseDto;
import conditioner.service.ValidationService;
import conditioner.service.WorkersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/workers")
public class WorkersController {

    @Autowired
    WorkersService workersService;
    @Autowired
    ValidationService validationService;
    
   @GetMapping("/missed/{workerUuid}")
   public List<WorkersTypeMaintenanceResponseDto> getAllConditionerWithMissedTypeMaintenance(
           @PathVariable String workerUuid){
       validationService.checkWorker(workerUuid);
       return workersService.getAllConditionerWithMissedTypeMaintenance(workerUuid);
   }

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

    @PutMapping("/{recordUuid}/{workerUuid}")
    public WorkersTypeMaintenanceResponseDto makeTypeMaintenanceInWork( @PathVariable String recordUuid,
                                                                        @PathVariable String workerUuid){
        /**
         * проверка - может ли этот работник брать в работу эту запись
         */
       validationService.checkWorkerAndRecord(recordUuid, workerUuid);
       return workersService.getTypeMaintenanceInWork(recordUuid, workerUuid);
    }

}
