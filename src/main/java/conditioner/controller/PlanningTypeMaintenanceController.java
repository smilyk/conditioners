package conditioner.controller;

import conditioner.dto.DatesForPlanningDto;
import conditioner.dto.PlannedTypeMaintenanceDto;
import conditioner.service.PlanedService;
import conditioner.service.PlanningService;
import conditioner.dto.PlanningTypeMaintenanceConditioner;
import conditioner.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/planning")
public class PlanningTypeMaintenanceController{

    @Autowired
    PlanningService planningService;

    @Autowired
    ValidationService validationService;

    @Autowired
    PlanedService planedService;

    @GetMapping("/missed")
    public List<PlanningTypeMaintenanceConditioner> getAllConditionerWithMissedTypeMaintenance(){
        return planningService.getAllConditionerWithMissedTypeMaintenance();
    }

    @GetMapping()
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


    @PostMapping("/plan")
    public String plannedTypeMaintenance(@RequestBody PlannedTypeMaintenanceDto
                                                                        plannedTypeMaintenanceDto){
        return planedService.toPlanTypeMaintenance(plannedTypeMaintenanceDto);
    }

}
