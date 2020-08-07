package conditioner.controller;

import conditioner.service.PlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/planning")
public class PlanningTypeMaintenanceConditioner {

    @Autowired
    PlanningService planningService;

    @GetMapping("/missed")
    public List<conditioner.dto.PlanningTypeMaintenanceConditioner> getAllConditionerWithMissedTypeMaintenance(){
        return planningService.getAllConditionerWithMissedTypeMaintenance();
    }


}
