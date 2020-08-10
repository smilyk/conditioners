package conditioner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import conditioner.constants.Messages;
import conditioner.dto.RequestForBusyWorkersDto;
import conditioner.dto.UserDetailsRequestModel;
import conditioner.dto.UserDto;
import conditioner.dto.WorkerDto;
import conditioner.enums.RoleEnum;
import conditioner.exceptions.ConditionerException;
import conditioner.model.BusyWorkerEntity;
import conditioner.model.TypeMaintenanceEntity;
import conditioner.model.UserEntity;
import conditioner.repository.BusyWorkersRepository;
import conditioner.repository.UserRepository;
import conditioner.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private static final RoleEnum ROLE = RoleEnum.MANAGER;
    private ObjectMapper mapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();

    @Value("${work_dat_finish}")
    private Integer workDayFinishTime;

    @Autowired
    Utils utils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BusyWorkersRepository busyWorkersRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PlanedService planedService;

    public UserDto createUser(UserDetailsRequestModel user) {
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        String publicUserId = utils.createRandomUuid();
        userEntity.setUserUuid(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        UserEntity storedUserDetails = userRepository.save(userEntity);
        LOGGER.info(Messages.USER_WITH_EMAIL + storedUserDetails.getEmail() + Messages.CREATED);
        UserDto userDto = modelMapper.map(storedUserDetails, UserDto.class);
        return userDto;
    }

    public List<WorkerDto> getAllNotBusyUsers(RequestForBusyWorkersDto request) {

        List<UserEntity> userEntity = getUserEntities();
        List<BusyWorkerEntity> busyWorkersList = busyWorkersRepository.findAll();
        //getting finish time for workers
        LocalDateTime startWorkTime = request.getStartDate();
        TypeMaintenanceEntity typeMaintenanceEntity = planedService.getTypeMaintenanceEntity(request.getUuidTypeMaintenance());
        int howTimeShouldWork = typeMaintenanceEntity.getPeopleHours()
                / request.getCountOfWorkers();
        //во сколько начинается работа над этим кондиционером
        int timeSpend = getTimeSpend(howTimeShouldWork, startWorkTime.getHour());
        LocalDateTime finishWorkTime = planedService.createFinishTime(startWorkTime, timeSpend, 1);
//        лист  всех времен, которые заняты
        List<LocalDateTime> busyTime = new ArrayList();
        while (startWorkTime.isBefore(finishWorkTime)) {
            busyTime.add(startWorkTime);
            startWorkTime = startWorkTime.plusHours(1);
        }
//        лист где key = id пользователя, value = часы, когда он занят
        Map<String, List> timeOfBusyWorkersMap = getMapOfBusyUsersWithTime(busyWorkersList);
//      лист для удаления сотрудников (тех), которые заняты
        Set<String> workersForDeletedFromUsersList = getListOfBusyWorkersForDeleted(busyTime, timeOfBusyWorkersMap);
//        чистка списка свободных сотрудников
        userEntity = getFreeUsersEntities(userEntity, workersForDeletedFromUsersList);

        List<WorkerDto> freeUsersDto = userEntity.stream().map(this::fromUserToUserDto).collect(Collectors.toList());
        LOGGER.info(Messages.FREE_WORKERS + freeUsersDto);
        return freeUsersDto;
    }

    private List<UserEntity> getFreeUsersEntities(List<UserEntity> userEntity, Set<String> workersForDeletedFromUsersList) {
        for (String x : workersForDeletedFromUsersList) {
            userEntity = userEntity.stream().filter(a -> !x.equals(a.getUserUuid())).collect(Collectors.toList());
        }
        return userEntity;
    }

    private Set<String> getListOfBusyWorkersForDeleted(List<LocalDateTime> busyTime, Map<String, List> timeOfBusyWorkersMap) {
        Set<String> workersForDeletedFromUsersList = new HashSet();
        for (LocalDateTime b : busyTime) {
            for (Map.Entry<String, List> t : timeOfBusyWorkersMap.entrySet()) {
                if (t.getValue().contains(b)) {
                    workersForDeletedFromUsersList.add(t.getKey());
                }
            }
        }
        return workersForDeletedFromUsersList;
    }

    private Map<String, List> getMapOfBusyUsersWithTime(List<BusyWorkerEntity> busyWorkersList) {
        List hours = new LinkedList();
        Map<String, List> timeOfBusyWorkersMap = new HashMap<>();
        for (BusyWorkerEntity workHours : busyWorkersList) {
            hours.clear();
            while (workHours.getStartTime().isBefore(workHours.getFinishTime())) {
                hours.add(workHours.getStartTime());
                workHours.setStartTime(workHours.getStartTime().plusHours(1));
            }
            List hoursForMap = new ArrayList();
            hoursForMap.addAll(hours);
            timeOfBusyWorkersMap.put(workHours.getWorkerUuid(), hoursForMap);
        }
        return timeOfBusyWorkersMap;
    }

    private int getTimeSpend(int howTimeShouldWork, int startHours) {
        return howTimeShouldWork - (workDayFinishTime - startHours);
    }

    private List<UserEntity> getUserEntities() {
        List<UserEntity> userEntity = userRepository.findByRole(ROLE);
        if (userEntity.isEmpty()) {
            LOGGER.error("Free workers not found in this time");
            throw new ConditionerException("Free workers not found in this time");
        }
        return userEntity;
    }

    private WorkerDto fromUserToUserDto(UserEntity userEntity) {
        return modelMapper.map(userEntity, WorkerDto.class);
    }
}
