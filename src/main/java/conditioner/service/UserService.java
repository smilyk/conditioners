package conditioner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import conditioner.dto.DatesForPlanningDto;
import conditioner.dto.UserDetailsRequestModel;
import conditioner.dto.UserDto;
import conditioner.exceptions.ConditionerException;
import conditioner.model.UserEntity;
import conditioner.repository.UserRepository;
import conditioner.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {


    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private ObjectMapper mapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    Utils utils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDto createUser(UserDetailsRequestModel user) {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        String publicUserId = utils.createRandomUuid();
        userEntity.setUserUuid(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        UserEntity storedUserDetails = userRepository.save(userEntity);
        UserDto userDto = modelMapper.map(storedUserDetails, UserDto.class);
        return userDto;
    }

    public List<UserDto> getAllNotBusyUsers(DatesForPlanningDto dates) {
        //TODO переписать так, что бы доставались только те сотрудники, которые НЕ заняты в указанное время
//        TODO - only Managers!!!
        List<UserEntity> userEntity = userRepository.findAll();
        if(userEntity.isEmpty()){
//            TODO LOGGER + normal Exception
            throw new ConditionerException("Users not found in this time");
        }
        List<UserDto> usersDto = userEntity.stream().map(this::fromUserToUserDto).collect(Collectors.toList());
        return usersDto;
    }

    private UserDto fromUserToUserDto(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }
}
