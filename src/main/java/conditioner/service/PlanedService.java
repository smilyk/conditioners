package conditioner.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PlanedService {

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

        TypeMaintenanceEntity typeMaintenanceEntity = typeMaintenanceRepository
                .findByUuidTypeMaintenance(plannedTypeMaintenanceDto.getTypeMaintenance().getUuidTypeMaintenance())
                .orElse(null);
        if(typeMaintenanceEntity == null){
            throw new ConditionerException(" wrong type maintenance");
        }

        int howTimeShouldWork = typeMaintenanceEntity.getPeopleHours()
                /plannedTypeMaintenanceDto.getWorkers().size();

        //        во сколько начинается работа над этим кондиуионером
        int startHours = plannedTypeMaintenanceDto.getStartTime().getHour();
//        сколько времени осталось до конца рабочего дня т е сколько отработал сегодня
        int timeSpend = howTimeShouldWork - (workDayFinishTime - startHours);
        int countWorkedDays = 1;
        while(timeSpend >= howHoursWorkInDay){

//        сколько времени осталось на завтра
            timeSpend = timeSpend - workDayStartTime;
            countWorkedDays++;
        }
        LocalDateTime finishTime = null;
        if(timeSpend == 0){
            finishTime = plannedTypeMaintenanceDto.getStartTime().toLocalDate().atStartOfDay()
                    .plusDays(countWorkedDays).minusDays(1).plusHours(workDayFinishTime);
        }else{
            finishTime = plannedTypeMaintenanceDto.getStartTime().toLocalDate().atStartOfDay()
                    .plusDays(countWorkedDays).plusHours(timeSpend + workDayStartTime);
        }
        System.err.println(finishTime);
        //добавили запись в таблицу занятых работников
        for(WorkerDto worker : plannedTypeMaintenanceDto.getWorkers()) {
            BusyWorkerEntity busyWorkerEntity = BusyWorkerEntity.builder()
                    .uuid(utils.createRandomUuid())
                    .workerUuid(worker.getUserUuid())
                    .startTime(plannedTypeMaintenanceDto.getStartTime())
                    .finishTime(finishTime)
                    .build();
            busyWorkersRepository.save(busyWorkerEntity);
        }

        Optional<ForPlanningTypeMaintenanceEntity> optionalForPlanningTypeMaintenanceEntity =
                forPlanningTypeMaintenanceRepository
                .findByUuidRecord(plannedTypeMaintenanceDto.getPlanningRecordUuid());
        if(!optionalForPlanningTypeMaintenanceEntity.isPresent()){
            throw new ConditionerException("notFound");
            //TODO LOGGER + NormalException
        }
       // обновили запись в таблице - для планирования
        ForPlanningTypeMaintenanceEntity forPlanningTypeMaintenanceEntity =
                optionalForPlanningTypeMaintenanceEntity.get();
        forPlanningTypeMaintenanceEntity.setLastTypeMaintenanceDate(
                plannedTypeMaintenanceDto.getStartTime()
        );
        Integer hours = typeMaintenanceEntity.getHoursBeforeTypeMaintenance();
        LocalDateTime nextDate = finishTime.plusHours(hours);
        forPlanningTypeMaintenanceEntity.setNextTypeMaintenanceDate(nextDate);
        forPlanningTypeMaintenanceEntity.setLastTypeMaintenanceDate(finishTime);
        ForPlanningTypeMaintenanceEntity newForPlanningTypeMaintenanceEntity = forPlanningTypeMaintenanceRepository
                .save(forPlanningTypeMaintenanceEntity);

        //добавили запись в таблицу где написаны записи, которые запланированы
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

        return "next type maintenance for conditioner " + plannedTypeMaintenanceDto.getNameConditioner() +
                " with inventory number " + plannedTypeMaintenanceDto.getInventoryNumber() +
                " was planning on date " + newForPlanningTypeMaintenanceEntity.getNextTypeMaintenanceDate();
    }
}
