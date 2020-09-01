package conditioner.controller;

import conditioner.dto.RequestForBusyWorkersDto;
import conditioner.dto.UserDetailsRequestModel;
import conditioner.dto.UserDto;
import conditioner.dto.WorkerDto;
import conditioner.service.UserService;
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
@RequestMapping("/users")
@Api(value = "Users", description = "methods that collect data about the user ")

public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ValidationService validationService;

    @ApiOperation(value = "Adding a new user to DataBase")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added user"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PostMapping()
    public UserDto createUser(@Valid @RequestBody UserDetailsRequestModel user){
        validationService.validUser(user);
        return userService.createUser(user);
    }


    @ApiOperation(value = "Getting all not busy  user (by Date)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully getting not busy user user"),
//            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
//            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @PutMapping("/all-not-busy")
    public List<WorkerDto> getUser(@RequestBody RequestForBusyWorkersDto request){
        return userService.getAllNotBusyUsers(request);
    }
}
