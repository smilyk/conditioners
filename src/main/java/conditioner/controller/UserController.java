package conditioner.controller;

import conditioner.dto.DatesForPlanningDto;
import conditioner.dto.UserDetailsRequestModel;
import conditioner.dto.UserDto;
import conditioner.model.UserEntity;
import conditioner.service.UserService;
import conditioner.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ValidationService validationService;
    
    @PostMapping()
    public UserDto createUser(@RequestBody UserDetailsRequestModel user){
        validationService.validUser(user);
        return userService.createUser(user);
    }

    @GetMapping("/all-not-busy")
    public List<UserDto> getUser(@RequestBody DatesForPlanningDto dates){
        return userService.getAllNotBusyUsers(dates);
    }
}
