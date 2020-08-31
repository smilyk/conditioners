package conditioner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import conditioner.constants.Messages;
import conditioner.dto.PlannedTypeMaintenanceDto;
import conditioner.dto.WorkerDto;
import conditioner.exceptions.ConditionerException;
import conditioner.model.BusyWorkerEntity;
import conditioner.model.ForPlanningTypeMaintenanceEntity;
import conditioner.model.InWorkEntity;
import conditioner.model.TypeMaintenanceEntity;
import conditioner.repository.BusyWorkersRepository;
import conditioner.repository.ForPlanningTypeMaintenanceRepository;
import conditioner.repository.InWorkRepository;
import conditioner.repository.TypeMaintenanceRepository;
import conditioner.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PlanedService {

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
    private Utils utils;

    @Autowired
    private InWorkRepository inWorkRepository;

    @Autowired
    private BusyWorkersRepository busyWorkersRepository;

    @Autowired
    ForPlanningTypeMaintenanceRepository forPlanningTypeMaintenanceRepository;

    @Autowired
    TypeMaintenanceRepository typeMaintenanceRepository;

//TODO что делаем, если время то позде чем конец рабочего дня - validator

    public String toPlanTypeMaintenance(PlannedTypeMaintenanceDto plannedTypeMaintenanceDto) {
        //получение даты окончания работ
        LocalDateTime finishTime = getFinishDate(plannedTypeMaintenanceDto);
        //получили данные ТО
        TypeMaintenanceEntity typeMaintenanceEntity = getTypeMaintenanceEntity(plannedTypeMaintenanceDto
                .getTypeMaintenance().getUuidTypeMaintenance());
        //добавили запись в таблицу занятых работников
        addWorkersToBusyWorkersTable(plannedTypeMaintenanceDto, finishTime);
       // обновили запись в таблице - для планирования
        ForPlanningTypeMaintenanceEntity newForPlanningTypeMaintenanceEntity = saveForPlanningTypeMaintenanceEntity(
                plannedTypeMaintenanceDto, finishTime, typeMaintenanceEntity);
        //добавили запись в таблицу где написаны записи, которые запланированы
        saveInWorkEntity(plannedTypeMaintenanceDto, finishTime);

        return "next type maintenance for conditioner " + plannedTypeMaintenanceDto.getNameConditioner() +
                " with inventory number " + plannedTypeMaintenanceDto.getInventoryNumber() +
                " was planning on date " + newForPlanningTypeMaintenanceEntity.getNextTypeMaintenanceDate();
    }

    private void saveInWorkEntity(PlannedTypeMaintenanceDto plannedTypeMaintenanceDto, LocalDateTime finishTime) {
        InWorkEntity inWorkEntity = InWorkEntity.builder()
                .recordsUuid(utils.createRandomUuid())
                .inventoryNumber(plannedTypeMaintenanceDto.getInventoryNumber())
                .typeMaintenanceUuid(plannedTypeMaintenanceDto.getTypeMaintenance().getUuidTypeMaintenance())
                .startTime(plannedTypeMaintenanceDto.getStartTime())
                .finishTime(finishTime)
                .firstWorkerUuid(plannedTypeMaintenanceDto.getWorkers().get(0).getUserUuid())
                .secondWorkerUuid(plannedTypeMaintenanceDto.getWorkers().get(1).getUserUuid())
                //will be true when worker will take it in the work
                .in_work(false)
                //will be true when worker will be finish the work
                .done(false)
                .build();
        InWorkEntity newEntity = inWorkRepository.save(inWorkEntity);
        LOGGER.info("save inWorkEntity with UUID {}", newEntity.recordsUuid);
    }

    private ForPlanningTypeMaintenanceEntity saveForPlanningTypeMaintenanceEntity(
            PlannedTypeMaintenanceDto plannedTypeMaintenanceDto, LocalDateTime finishTime,
            TypeMaintenanceEntity typeMaintenanceEntity) {
        Optional<ForPlanningTypeMaintenanceEntity> optionalForPlanningTypeMaintenanceEntity =
                forPlanningTypeMaintenanceRepository
                        .findByUuidRecord(plannedTypeMaintenanceDto.getPlanningRecordUuid());
        if(!optionalForPlanningTypeMaintenanceEntity.isPresent()){
            LOGGER.error(Messages.RECORD_FOR_PLANNING + Messages.WITH_ID +
                    plannedTypeMaintenanceDto.getPlanningRecordUuid() + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.RECORD_FOR_PLANNING + Messages.WITH_ID +
                    plannedTypeMaintenanceDto.getPlanningRecordUuid() + Messages.NOT_FOUND);
        }

        ForPlanningTypeMaintenanceEntity forPlanningTypeMaintenanceEntity =
                optionalForPlanningTypeMaintenanceEntity.get();
        forPlanningTypeMaintenanceEntity.setLastTypeMaintenanceDate(
                plannedTypeMaintenanceDto.getStartTime()
        );
        Integer hours = typeMaintenanceEntity.getHoursBeforeTypeMaintenance();
        LocalDateTime nextDate = finishTime.plusHours(hours);
        forPlanningTypeMaintenanceEntity.setNextTypeMaintenanceDate(nextDate);
        forPlanningTypeMaintenanceEntity.setLastTypeMaintenanceDate(finishTime);
        LOGGER.info(Messages.RECORD_FOR_PLANNING + Messages.WITH_ID
                + forPlanningTypeMaintenanceEntity.getUuidRecord() + Messages.UPDATED);
        return forPlanningTypeMaintenanceRepository
                .save(forPlanningTypeMaintenanceEntity);
    }

    private void addWorkersToBusyWorkersTable(PlannedTypeMaintenanceDto plannedTypeMaintenanceDto, LocalDateTime finishTime) {
        for(WorkerDto worker : plannedTypeMaintenanceDto.getWorkers()) {
            BusyWorkerEntity busyWorkerEntity = BusyWorkerEntity.builder()
                    .uuid(utils.createRandomUuid())
                    .workerUuid(worker.getUserUuid())
                    .startTime(plannedTypeMaintenanceDto.getStartTime())
                    .finishTime(finishTime)
                    .build();
            BusyWorkerEntity newBusyWorkerEntity = busyWorkersRepository.save(busyWorkerEntity);
            LOGGER.info(Messages.BUSY_WORKER + Messages.WITH_ID + newBusyWorkerEntity.getUuid()
            + Messages.CREATED);
        }
    }

    private LocalDateTime getFinishDate(PlannedTypeMaintenanceDto plannedTypeMaintenanceDto) {

        TypeMaintenanceEntity typeMaintenanceEntity = getTypeMaintenanceEntity(plannedTypeMaintenanceDto
                .getTypeMaintenance().getUuidTypeMaintenance());

        int howTimeShouldWork = typeMaintenanceEntity.getPeopleHours()
                / plannedTypeMaintenanceDto.getWorkers().size();

        //        во сколько начинается работа над этим кондиуионером
        int startHours = plannedTypeMaintenanceDto.getStartTime().getHour();
        //        сколько времени осталось до конца рабочего дня т е сколько отработал сегодня
        int timeSpend = howTimeShouldWork - (workDayFinishTime - startHours);
        int countWorkedDays = 1;
        //время начала
        LocalDateTime startTime = plannedTypeMaintenanceDto.getStartTime();
        LocalDateTime finishTime = createFinishTime(startTime, timeSpend, countWorkedDays);
        LOGGER.info(Messages.DATE_OF_START_WORK + startTime + "," +
                Messages.DATE_OF_FINISH_WORK + finishTime.toLocalDate());
        return createFinishTime(startTime, timeSpend, countWorkedDays);
    }

    public LocalDateTime createFinishTime(LocalDateTime startTime, int timeSpend, int countWorkedDays) {
        while (timeSpend >= howHoursWorkInDay) {

//        сколько времени осталось на завтра
            timeSpend = timeSpend - workDayStartTime;
            countWorkedDays++;
        }
        LocalDateTime finishTime = null;
        if (timeSpend == 0) {
            finishTime = startTime.toLocalDate().atStartOfDay()
                    .plusDays(countWorkedDays).minusDays(1).plusHours(workDayFinishTime);
        } else {
            finishTime = startTime.toLocalDate().atStartOfDay()
                    .plusDays(countWorkedDays).plusHours(timeSpend + workDayStartTime);
        }
//        LOGGER.info(Messages.DATE_OF_START_WORK + startTime + "," +
//                Messages.DATE_OF_FINISH_WORK + finishTime.toLocalDate());
        return finishTime;
    }

    public TypeMaintenanceEntity getTypeMaintenanceEntity(String uuidTypeMaintenance ) {
        TypeMaintenanceEntity typeMaintenanceEntity = typeMaintenanceRepository
                .findByUuidTypeMaintenance(uuidTypeMaintenance)
                .orElse(null);
        if (typeMaintenanceEntity == null) {
            LOGGER.error(Messages.CHECK_UNIQUE_TYPE_MAINTENANCE +
                    uuidTypeMaintenance +
                    Messages.NOT_FOUND);
            throw new ConditionerException(Messages.CHECK_UNIQUE_TYPE_MAINTENANCE +
                    uuidTypeMaintenance +
                    Messages.NOT_FOUND);
        }
        return typeMaintenanceEntity;
    }
}
