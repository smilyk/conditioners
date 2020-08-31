package conditioner.controller;


import conditioner.dto.TypeMaintenanceDto;
import conditioner.service.MaintenanceServiceImpl;
import conditioner.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/maintenance")
public class TypeMaintenanceController {
    @Autowired
    MaintenanceServiceImpl maintenanceService;
    @Autowired
    ValidationService validationService;
    
    @PostMapping()
    public TypeMaintenanceDto createTypeMaintenance(@Valid  @RequestBody TypeMaintenanceDto typeMaintenanceDto) {
        validationService.validTypeMaintenance(typeMaintenanceDto);
        return maintenanceService.createTypeMaintenance(typeMaintenanceDto);
    }
    
    @GetMapping("/{typeMaintenanceUuid}")
    public TypeMaintenanceDto getTypeMaintenance(@PathVariable String typeMaintenanceUuid){
        return maintenanceService.getTypeMaintenanceById(typeMaintenanceUuid);
    }

    @GetMapping()
    public List<TypeMaintenanceDto> getAllTypeMalignancies(){

        return maintenanceService.getAllTypeMaintenances();
    }
    @DeleteMapping("/{typeMaintenanceUuid}")
    public TypeMaintenanceDto deleteTypeMaintenance(@PathVariable String typeMaintenanceUuid){
        return maintenanceService.deleteTypeMaintenance(typeMaintenanceUuid);
    }

}
