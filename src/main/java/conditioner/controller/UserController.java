package conditioner.controller;

import conditioner.dto.RequestForBusyWorkersDto;
import conditioner.dto.UserDetailsRequestModel;
import conditioner.dto.UserDto;
import conditioner.dto.WorkerDto;
import conditioner.service.UserService;
import conditioner.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ValidationService validationService;
    
    @PostMapping()
    public UserDto createUser(@Valid @RequestBody UserDetailsRequestModel user){
        validationService.validUser(user);
        return userService.createUser(user);
    }

    @GetMapping("/all-not-busy")
    public List<WorkerDto> getUser(@RequestBody RequestForBusyWorkersDto request){
        return userService.getAllNotBusyUsers(request);
    }
}
