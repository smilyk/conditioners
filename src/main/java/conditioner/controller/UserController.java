package conditioner.controller;

import conditioner.dto.UserDetailsRequestModel;
import conditioner.dto.UserDto;
import conditioner.model.UserEntity;
import conditioner.service.UserService;
import conditioner.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
