package conditioner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import conditioner.constants.Messages;
import conditioner.dto.ConditionerDto;
import conditioner.exceptions.ConditionerException;
import conditioner.model.ConditionerEntity;
import conditioner.model.ForPlanningTypeMaintenanceEntity;
import conditioner.model.TypeMaintenanceEntity;
import conditioner.repository.ConditionerRepository;
import conditioner.repository.ForPlanningTypeMaintenanceRepository;
import conditioner.repository.TypeMaintenanceRepository;
import conditioner.utils.Utils;
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
public class ConditionerServiceImpl {
    @Autowired
    ConditionerRepository conditionerRepository;
    @Autowired
    Utils utils;
    @Autowired
    TypeMaintenanceRepository typeMaintenanceRepository;
    @Autowired
    ForPlanningTypeMaintenanceRepository forPlanningTypeMaintenanceRepository;


    private static final Logger LOGGER = LogManager.getLogger(ConditionerServiceImpl.class);

    private ObjectMapper mapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();


    public ConditionerDto createConditioner(ConditionerDto conditionerDto) {
        try {
//        TODO написать проверку - существует ли то ТО которое собираются подключить к кондиционеру
        ConditionerEntity conditioner = conditionerDtoToEntity(conditionerDto);
        conditioner.setStart(false);
        conditioner.setDeleted(false);
        conditioner.setUuidConditioner(utils.createRandomUuid());
        String conditionerUuid = conditionerRepository.save(conditioner).getUuidConditioner();
        conditionerDto.setUuidConditioner(conditionerUuid);
            LOGGER.info("Conditioner created {}", mapper.writeValueAsString(conditioner));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ConditionerException(e.getMessage());
        }
        return conditionerDto;
    }

    public ConditionerDto getConditionerById(String conditionerUuid) {
//        Optional<ConditionerEntity> optionalConditionerEntity = conditionerRepository.findByUuidConditionerAndDeleted(conditionerUuid, false);
        Optional<ConditionerEntity> optionalConditionerEntity = conditionerRepository.findByUuidConditioner(conditionerUuid);
        if (!optionalConditionerEntity.isPresent()) {
            LOGGER.error(Messages.CONDITIONER + Messages.WITH_ID + conditionerUuid + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.CONDITIONER + Messages.WITH_ID + conditionerUuid + Messages.NOT_FOUND);
        }
        ConditionerEntity conditioner = optionalConditionerEntity.get();
        ConditionerDto conditionerDto = conditionerToDto(conditioner);
        try {
            LOGGER.info("Conditioner with id {} found", conditionerUuid);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ConditionerException(e.getMessage());
        }
        return conditionerDto;
    }

    public List<ConditionerDto> getAllConditioners() {

            List<ConditionerEntity> conditionerEntities = conditionerRepository.findAll();
            List<ConditionerDto> conditionerDtos = conditionerEntities.stream().map(this::conditionerToDto).collect(Collectors.toList());
            LOGGER.info(Messages.LIST + Messages.CONDITIONER + Messages.FOUND);
        return conditionerDtos;
    }

    private ConditionerDto conditionerToDto(ConditionerEntity conditioner) {
        return modelMapper.map(conditioner, ConditionerDto.class);
    }

    private ConditionerEntity conditionerDtoToEntity(ConditionerDto conditionerDto) {
        return modelMapper.map(conditionerDto, ConditionerEntity.class);
    }

    public ConditionerDto deleteConditionerById(String conditionerUuid) {
        Optional<ConditionerEntity> optionalConditionerEntity = conditionerRepository.findByUuidConditioner(conditionerUuid);
        if (!optionalConditionerEntity.isPresent()) {
            LOGGER.error(Messages.CONDITIONER + Messages.WITH_ID + conditionerUuid + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.CONDITIONER + Messages.WITH_ID + conditionerUuid + Messages.NOT_FOUND);
        }
        ConditionerEntity conditioner = optionalConditionerEntity.get();
        conditioner.setDeleted(true);
        conditionerRepository.save(conditioner);
        ConditionerDto conditionerDto = conditionerToDto(conditioner);
        try {
            LOGGER.info("Conditioner with id {} deleted ", conditionerUuid);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ConditionerException(e.getMessage());
        }
        return conditionerDto;
    }

    public String startWorkConditioner(String conditionerUuid) {
        Optional<ConditionerEntity> optionalConditionerEntity = conditionerRepository.findByUuidConditionerAndDeleted(
                conditionerUuid, false
        );
        if (optionalConditionerEntity.isPresent()) {
            ConditionerEntity conditionerEntity = optionalConditionerEntity.get();
            if(conditionerEntity.getMaintenance().size() == 0){
                LOGGER.info(Messages.CONDITIONER_WITHOUT_TYPE_MAINTENANCE + conditionerEntity.getUuidConditioner());
            }
            if (conditionerEntity.getStart()) {
                LOGGER.info(Messages.CHECK_UNIQUE_CONDITIONER + conditionerEntity.getInventoryNumber() +
                        Messages.WORKING_NOW);
                throw new ConditionerException(Messages.CHECK_UNIQUE_CONDITIONER + conditionerEntity.getInventoryNumber()
                        + Messages.WORKING_NOW);
            }
            conditionerEntity.setStart(true);
            conditionerEntity.setStartDate(LocalDateTime.now());
            conditionerRepository.save(conditionerEntity);
            /**
             * making record to table for planning
             */
            createForPlanningTypeMaintenance(conditionerEntity);
            LOGGER.info(Messages.CHECK_UNIQUE_CONDITIONER + conditionerEntity.getInventoryNumber() +
                    Messages.STARTED_WORK);
            return Messages.CHECK_UNIQUE_CONDITIONER + conditionerEntity.getInventoryNumber() +
                    Messages.STARTED_WORK;
        }
        LOGGER.error(Messages.CHECK_VALID_CONDITIONER + conditionerUuid + Messages.NOT_FOUND);
        throw new ConditionerException(Messages.CHECK_VALID_CONDITIONER + conditionerUuid + Messages.NOT_FOUND);
    }

    public ConditionerDto addTypeMaintenanceToConditioner(String conditionerUuid, String typeMaintenanceUuid) {
        Optional<ConditionerEntity> optionalConditionerEntity = checkConditionerEntity(conditionerUuid);
        Optional<TypeMaintenanceEntity> optionalTypeMaintenanceEntity = checkTypeMaintenanceEntity(conditionerUuid, typeMaintenanceUuid);
        ConditionerEntity conditionerEntity = optionalConditionerEntity.get();

        List<TypeMaintenanceEntity> typeMaintenanceEntityList = conditionerEntity.getMaintenance();
        if (typeMaintenanceEntityList.stream().filter(x ->
                x.getUuidTypeMaintenance().equals(typeMaintenanceUuid)).count() != 0) {
            LOGGER.info(Messages.CHECK_UNIQUE_TYPE_MAINTENANCE + typeMaintenanceUuid +
                    Messages.EXISTS + Messages.IN +
                    Messages.CHECK_VALID_CONDITIONER
                    + conditionerUuid);
            throw new ConditionerException(Messages.CHECK_UNIQUE_TYPE_MAINTENANCE + typeMaintenanceUuid +
                    Messages.EXISTS + Messages.IN +
                    Messages.CHECK_VALID_CONDITIONER
                    + conditionerUuid);
        }
        typeMaintenanceEntityList.add(optionalTypeMaintenanceEntity.get());
        conditionerEntity.setMaintenance(typeMaintenanceEntityList);
        conditionerRepository.save(conditionerEntity);
        ConditionerDto conditionerDto = conditionerToDto(conditionerEntity);
        LOGGER.info(Messages.CHECK_UNIQUE_TYPE_MAINTENANCE +
                optionalTypeMaintenanceEntity.get().getUuidTypeMaintenance() + Messages.ADDED +
                Messages.CHECK_UNIQUE_CONDITIONER + conditionerEntity.getUuidConditioner());
//        TODO не добавлять ТО к работающему кондиционеру - реализоыать проверку.
//        TODO можем останавливать и запускать кондиционер. Продумать как учитывать часы, которые уже отработаны
//        createForPlanningTypeMaintenance(conditionerEntity);
        return conditionerDto;
    }

    private void createForPlanningTypeMaintenance(ConditionerEntity conditionerEntity) {

        List<TypeMaintenanceEntity> listOdMaintenancy = conditionerEntity.getMaintenance();
        for (TypeMaintenanceEntity entity : listOdMaintenancy) {
            Integer hoursBeforeTypeMaintenance = entity.getHoursBeforeTypeMaintenance();
            LocalDateTime startDate = conditionerEntity.getStartDate();
            LocalDateTime nextTypeMaintenancy = startDate.plusHours(hoursBeforeTypeMaintenance);
            ForPlanningTypeMaintenanceEntity en = ForPlanningTypeMaintenanceEntity.builder()
                    .inventoryNumber(conditionerEntity.getInventoryNumber())
                    .lastTypeMaintenanceDate(conditionerEntity.getStartDate())
                    .nameConditioner(conditionerEntity.getNameConditioner())
                    .nextTypeMaintenanceDate(nextTypeMaintenancy)
                    .place(conditionerEntity.getPlace())
                    .uuidConditioner(conditionerEntity.getUuidConditioner())
                    .uuidTypeMaintenance(entity.getUuidTypeMaintenance())
                    .uuidRecord(utils.createRandomUuid())
                    .build();
            forPlanningTypeMaintenanceRepository.save(en);
            LOGGER.info(Messages.RECORD_FOR_PLANNING + Messages.CHECK_UNIQUE_CONDITIONER +
                    conditionerEntity.getInventoryNumber() + Messages.CREATED);
        }
    }

    private Optional<TypeMaintenanceEntity> checkTypeMaintenanceEntity(String conditionerUuid, String typeMaintenanceUuid) {
        Optional<TypeMaintenanceEntity> optionalTypeMaintenanceEntity = typeMaintenanceRepository
                .findByUuidTypeMaintenanceAndDeleted(typeMaintenanceUuid, false);
        if (!optionalTypeMaintenanceEntity.isPresent()) {
            LOGGER.error(Messages.CHECK_UNIQUE_TYPE_MAINTENANCE + conditionerUuid + Messages.NOT_FOUND);
        }
        return optionalTypeMaintenanceEntity;
    }

    private Optional<ConditionerEntity> checkConditionerEntity(String conditionerUuid) {
        Optional<ConditionerEntity> optionalConditionerEntity = conditionerRepository.findByUuidConditionerAndDeleted(
                conditionerUuid, false
        );
        if (!optionalConditionerEntity.isPresent()) {
            LOGGER.error(Messages.CHECK_VALID_CONDITIONER + conditionerUuid + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.CHECK_VALID_CONDITIONER + conditionerUuid + Messages.NOT_FOUND);
        }
        return optionalConditionerEntity;
    }


    public List<ConditionerDto> getAllNotStartedConditioner() {
        List<ConditionerEntity> notStartedConditioners = conditionerRepository.findByStart(false);
        List<ConditionerDto> conditionerDtoList = notStartedConditioners.stream()
                .filter(x -> x.getMaintenance().size() != 0)
                .map(
                        this::conditionerToDto)
                .collect(Collectors.toList());
        if(conditionerDtoList.size() ==0 ){
            LOGGER.info(Messages.NOT_STARTED + Messages.CONDITIONER + Messages.NOT_FOUND);
            return conditionerDtoList;
        }
        LOGGER.info(Messages.NOT_STARTED + Messages.CONDITIONER + Messages.FOUND +
                conditionerDtoList);
        return conditionerDtoList;
    }

    public List<ConditionerDto> getAllNotTypeMaintenanceConditioners() {
        List<ConditionerEntity> allConditioners = conditionerRepository.findAll();
        List<ConditionerDto> conditionerDtoList = allConditioners.stream().filter(
                x -> x.getMaintenance().size() == 0
        ).map(this::conditionerToDto).collect(Collectors.toList());
        if(conditionerDtoList.size() ==0 ){
            LOGGER.info(  Messages.CONDITIONER + Messages.WITHOUT_TYPE_MAINTENANCE + Messages.NOT_FOUND);
            return conditionerDtoList;
        }
        LOGGER.info(Messages.CONDITIONER + Messages.WITHOUT_TYPE_MAINTENANCE + Messages.NOT_FOUND +
                conditionerDtoList);
        return conditionerDtoList;
    }
}
