package conditioner.controller;

import conditioner.dto.DatesForPlanningDto;
import conditioner.service.PlanningService;
import conditioner.dto.PlanningTypeMaintenanceConditioner;
import conditioner.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/planning")
public class PlanningTypeMaintenanceController{

    @Autowired
    PlanningService planningService;

    @Autowired
    ValidationService validationService;

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


}
