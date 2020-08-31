package conditioner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import conditioner.constants.Messages;
import conditioner.dto.DatesForPlanningDto;
import conditioner.dto.WorkerDto;
import conditioner.dto.WorkersTypeMaintenanceResponseDto;
import conditioner.enums.TypeMaintenanceStatus;
import conditioner.exceptions.ConditionerException;
import conditioner.model.*;
import conditioner.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WorkersService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private ObjectMapper mapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();

    @Value("${work_day_start}")
    private Integer workDayStartTime;

    @Value("${work_dat_finish}")
    private Integer workDayFinishTime;

    @Value("${work_in_day}")
    private Integer howHoursWorkInDay;

    @Autowired
    InWorkRepository inWorkRepository;
    @Autowired
    ConditionerRepository conditionerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TypeMaintenanceRepository typeMaintenanceRepository;
    @Autowired
    ArchiveRepository archiveRepository;

    @Autowired
    ValidationService validationService;

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

    public WorkersTypeMaintenanceResponseDto getTypeMaintenanceInWork(String recordUuid, String workerUuid) {
        InWorkEntity inWorkEntity = validationService.checkWorkerAndRecord(recordUuid, workerUuid);
        inWorkEntity.setIn_work(true);
        inWorkEntity.setStartTime(LocalDateTime.now());
        inWorkRepository.save(inWorkEntity);
        WorkersTypeMaintenanceResponseDto workersTypeMaintenanceResponseDto =
                getWorkersTypeMaintenanceResponseDtoFromInWorkEntity(inWorkEntity);
        LOGGER.info(Messages.WORKER_WITH_ID + workerUuid + Messages.GET_IN_WORK_RECORD + Messages.WITH_ID + recordUuid);
        return workersTypeMaintenanceResponseDto;
    }

    public WorkersTypeMaintenanceResponseDto getTypeMaintenanceDone(String recordUuid, String workerUuid) {
//        получение записи о том, какую работу будем делать
        InWorkEntity inWorkEntity = validationService.checkWorkerAndRecord(recordUuid, workerUuid);
//        получение записи о ТО которое будет делаться
        WorkersTypeMaintenanceResponseDto workersTypeMaintenanceResponseDto =
                getWorkersTypeMaintenanceResponseDtoFromInWorkEntity(inWorkEntity);
        LOGGER.info(Messages.WORKER_WITH_ID + workerUuid + Messages.FINISHED + recordUuid);
//        создание архивной записи
        createArchiveRecord(workersTypeMaintenanceResponseDto);
//        удаление из таблицв, в которой хранятся задачи, которые должны быть в работе
        inWorkRepository.delete(inWorkEntity);
        return workersTypeMaintenanceResponseDto;
    }

    private void createArchiveRecord(WorkersTypeMaintenanceResponseDto workersTypeMaintenanceResponseDto) {
//        в архиве должны храниться записи о том, сколько времени какой работник потратил на этот конкретный кондиционер
        Integer hours = getWorkedHours(workersTypeMaintenanceResponseDto.getStartTimes());
        ArchiveEntity archiveEntity = ArchiveEntity.builder()
                .description("")
                .firstWorkerUuid(workersTypeMaintenanceResponseDto.getWorkers().get(0).getUserUuid())
                .secondWorkerUuid(workersTypeMaintenanceResponseDto.getWorkers().get(0).getUserUuid())
                .inventoryNumber(workersTypeMaintenanceResponseDto.getInventoryNumber())
                .nameConditioner(workersTypeMaintenanceResponseDto.getNameConditioner())
                .place(workersTypeMaintenanceResponseDto.getPlace())
                .startWorkTime(workersTypeMaintenanceResponseDto.getStartTimes())
                .finishWorkTime(LocalDateTime.now())
                .status(TypeMaintenanceStatus.FINISH)
                .typeMaintenanceName(workersTypeMaintenanceResponseDto.getTypeMaintenanceName())
                .workedHours(hours)
                .build();
        archiveRepository.save(archiveEntity);
    }

    private Integer getWorkedHours(LocalDateTime startTimes) {

        LocalDateTime finishFirstDayHours = startTimes.toLocalDate().atTime(workDayFinishTime, 00, 00);
        long hoursFirstDay = getHoursFirstOrLastDay(startTimes, finishFirstDayHours);
        LocalDateTime startLastDayHourse = LocalDateTime.now().toLocalDate().atTime(workDayStartTime,
                00, 00);
        long hoursLastDay = getHoursFirstOrLastDay(startLastDayHourse, LocalDateTime.now());
        Duration duration = Duration.between(startTimes, LocalDateTime.now());
        long allWorkedDays = Math.abs(duration.toDays() -1);
        long workByDay = workDayFinishTime - workDayStartTime;

        return Math.toIntExact(((allWorkedDays * workByDay) + hoursFirstDay + hoursLastDay));
    }

    private long getHoursFirstOrLastDay(LocalDateTime startTimes, LocalDateTime finishTime) {
        Duration firstDayWorked = Duration.between(startTimes, finishTime);
//        сколько часов и минут отработано в первый день
        long hours = Math.abs(firstDayWorked.toHours());
        long minutes = Math.abs(firstDayWorked.toMinutes());
        if (minutes % 60 >= 30) {
            hours++;
        }
        return hours;
    }


    private WorkersTypeMaintenanceResponseDto getWorkersTypeMaintenanceResponseDtoFromInWorkEntity(InWorkEntity inWorkEntity) {
        ConditionerEntity conditioner = findConditioner(inWorkEntity.getInventoryNumber());
        List<UserEntity> users = findUser(inWorkEntity.getFirstWorkerUuid(), inWorkEntity.getSecondWorkerUuid());
        List<WorkerDto> workers = workersFromUsers(users);
        TypeMaintenanceEntity typeMaintenance = findTypeMaintenancy(inWorkEntity.getTypeMaintenanceUuid());

        return WorkersTypeMaintenanceResponseDto.builder()
                .inventoryNumber(inWorkEntity.getInventoryNumber())
                .nameConditioner(conditioner.getNameConditioner())
                .place(conditioner.getPlace())
                .startTimes(inWorkEntity.getStartTime())
                .typeMaintenanceName(inWorkEntity.getTypeMaintenanceUuid())
                .typeMaintenancePeopleHorse(typeMaintenance.getPeopleHours())
                .workers(workers)
                .planningRecordsUuid(inWorkEntity.getRecordsUuid())
                .build();

    }

    private List<WorkerDto> workersFromUsers(List<UserEntity> users) {
        List<WorkerDto> workers = new ArrayList<>(2);
        for(UserEntity user : users){
            workers.add(
                    WorkerDto.builder()
                            .userUuid(user.getUserUuid())
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
