package conditioner.controller;


import conditioner.dto.TypeMaintenanceDto;
import conditioner.service.MaintenanceServiceImpl;
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
@RequestMapping("/maintenance")
@Api(value = "Type Maintenance", description = "methods that collect data about the conditioners ")

public class TypeMaintenanceController {
    @Autowired
    MaintenanceServiceImpl maintenanceService;
    @Autowired
    ValidationService validationService;

    @ApiOperation(value = "Adding a new type maintenance to DataBase")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added type maintenance"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping()
    public TypeMaintenanceDto createTypeMaintenance(@Valid  @RequestBody TypeMaintenanceDto typeMaintenanceDto) {
        validationService.validTypeMaintenance(typeMaintenanceDto);
        return maintenanceService.createTypeMaintenance(typeMaintenanceDto);
    }

    @ApiOperation(value = "Getting a type maintenance from DB")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get type maintenance"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/{typeMaintenanceUuid}")
    public TypeMaintenanceDto getTypeMaintenance(@PathVariable String typeMaintenanceUuid){
        return maintenanceService.getTypeMaintenanceById(typeMaintenanceUuid);
    }


    @ApiOperation(value = "Getting all type maintenance from DB")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get all type maintenance"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping()
    public List<TypeMaintenanceDto> getAllTypeMaintenance(){
        return maintenanceService.getAllTypeMaintenances();
    }

    @ApiOperation(value = "Getting all not deleted type maintenance from DB")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get all type maintenance"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @GetMapping("/not-deleted")
    public List<TypeMaintenanceDto> getAllNotDeletedTypeMaintenance(){
        return maintenanceService.getAllNotDeletedTypeMaintenances();
    }

    @ApiOperation(value = "Delete type maintenance from DB")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted type maintenance"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @DeleteMapping("/{typeMaintenanceUuid}")
    public TypeMaintenanceDto deleteTypeMaintenance(@PathVariable String typeMaintenanceUuid){
        return maintenanceService.deleteTypeMaintenance(typeMaintenanceUuid);
    }

}
