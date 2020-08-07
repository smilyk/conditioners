package conditioner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import conditioner.dto.UserDetailsRequestModel;
import conditioner.dto.UserDto;
import conditioner.model.UserEntity;
import conditioner.repository.UserRepository;
import conditioner.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


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

}
