package conditioner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import conditioner.constants.Messages;
import conditioner.dto.DatesForPlanningDto;
import conditioner.dto.WorkerDto;
import conditioner.dto.response.WorkersTypeMaintenanceResponseDto;
import conditioner.exceptions.ConditionerException;
import conditioner.model.*;
import conditioner.repository.ConditionerRepository;
import conditioner.repository.InWorkRepository;
import conditioner.repository.TypeMaintenanceRepository;
import conditioner.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkersService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private ObjectMapper mapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    InWorkRepository inWorkRepository;
    @Autowired
    ConditionerRepository conditionerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TypeMaintenanceRepository typeMaintenanceRepository;

    public List<WorkersTypeMaintenanceResponseDto> getAllConditionerWithMissedTypeMaintenance(String workersUuid) {
        LocalDateTime now = LocalDateTime.now();
        List<InWorkEntity>  listInWorkTypeMaintenance= inWorkRepository.findByWorkersUuid(workersUuid, now,
                false);
        List<WorkersTypeMaintenanceResponseDto> plannedTypeMaintenanceDto = listInWorkTypeMaintenance.stream()
                .map(this::getWorkersTypeMaintenanceResponseDtoFromInWorkEntity).collect(Collectors.toList());
        LOGGER.info(Messages.RECORDS_FOR_GETTING_IN_WORK);
        return plannedTypeMaintenanceDto;
    }

    public List<WorkersTypeMaintenanceResponseDto> getAllRecordsForWork(DatesForPlanningDto dates, String workerUuid) {
        List<InWorkEntity> listInWorkTypeMaintenance =
                inWorkRepository.findByWorkersUuidAndDate(workerUuid, dates.getStartDate(),
                        dates.getFinishDate());
        List<WorkersTypeMaintenanceResponseDto> plannedTypeMaintenanceDto = listInWorkTypeMaintenance.stream()
                .map(this::getWorkersTypeMaintenanceResponseDtoFromInWorkEntity).collect(Collectors.toList());
        LOGGER.info(Messages.RECORDS_FOR_GETTING_IN_WORK);
        return plannedTypeMaintenanceDto;
    }

    private WorkersTypeMaintenanceResponseDto getWorkersTypeMaintenanceResponseDtoFromInWorkEntity(InWorkEntity inWorkEntity) {
        ConditionerEntity conditioner = findConditioner(inWorkEntity.getInventoryNumber());
        List<UserEntity> users = findUser(inWorkEntity.getFirstWorkerUuid(), inWorkEntity.getSecondWorkerUuid());
        List<WorkerDto> workers = workersFromUsers(users);
        TypeMaintenanceEntity typeMaintenance = findTypeMaintenancy(inWorkEntity.getTypeMaintenanceUuid());

        WorkersTypeMaintenanceResponseDto rez = WorkersTypeMaintenanceResponseDto.builder()
                .inventoryNumber(inWorkEntity.getInventoryNumber())
                .nameConditioner(conditioner.getNameConditioner())
                .place(conditioner.getPlace())
                .startTimes(inWorkEntity.getStartTime())
                .typeMaintenanceName(inWorkEntity.getTypeMaintenanceUuid())
                .typeMaintenancePeopleHorse(typeMaintenance.getPeopleHours())
                .workers(workers)
                .planningRecordsUuid(inWorkEntity.getRecordsUuid())
                .build();

        return rez;
    }

    private List<WorkerDto> workersFromUsers(List<UserEntity> users) {
        List<WorkerDto> workers = new ArrayList<>(2);
        for(UserEntity user : users){
            workers.add(
                    WorkerDto.builder()
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .email(user.getEmail())
                            .build()
            );
        }
        return workers;
    }

    private TypeMaintenanceEntity findTypeMaintenancy(String typeMaintenanceUuid) {
        Optional<TypeMaintenanceEntity> typeMaintenance = typeMaintenanceRepository.
                findByUuidTypeMaintenance(typeMaintenanceUuid);
        if(!typeMaintenance.isPresent()){
            LOGGER.error(Messages.TYPE_MAINTENANCE + Messages.WITH_ID + typeMaintenanceUuid + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.TYPE_MAINTENANCE + Messages.WITH_ID
                    + typeMaintenanceUuid + Messages.NOT_FOUND);
        }
        return  typeMaintenance.get();
    }

    private List<UserEntity> findUser(String firstWorkerUuid, String secondWorkerUuid) {
        Optional<UserEntity> firstUserEntity = userRepository.findByUserUuid(firstWorkerUuid);
        Optional<UserEntity> secondUserEntity = userRepository.findByUserUuid(secondWorkerUuid);
        if(!firstUserEntity.isPresent() || !secondUserEntity.isPresent()){
            LOGGER.error(Messages.USER_WITH_UUID + firstWorkerUuid + " or " + secondWorkerUuid + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.USER_WITH_UUID + firstWorkerUuid + " or "
                    + secondWorkerUuid + Messages.NOT_FOUND);
        }
        List<UserEntity> users = new ArrayList<>(2);
        users.add(firstUserEntity.get());
        users.add(secondUserEntity.get());
        return users;
    }

    private ConditionerEntity findConditioner(String inventoryNumber) {
        Optional<ConditionerEntity> conditionerEntity = conditionerRepository.findByInventoryNumber(inventoryNumber);
        if(!conditionerEntity.isPresent()){
            LOGGER.error(Messages.CONDITIONER + Messages.WITH_INVENTORY_NUMBER + inventoryNumber + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.CONDITIONER + Messages.WITH_INVENTORY_NUMBER
                    + inventoryNumber + Messages.NOT_FOUND);
        }
        return  conditionerEntity.get();
    }



}
