package conditioner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import conditioner.constants.Messages;
import conditioner.dto.ConditionerDto;
import conditioner.exceptions.ConditionerException;
import conditioner.model.ConditionerEntity;
import conditioner.model.TypeMaintenanceEntity;
import conditioner.repository.ConditionerRepository;
import conditioner.repository.TypeMaintenanceRepository;
import conditioner.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
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
    private static final Logger LOGGER = LogManager.getLogger(ConditionerServiceImpl.class);

    private ObjectMapper mapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();


    public ConditionerDto createConditioner(ConditionerDto conditionerDto) {

//        TODO написать проверку - существует ли то ТО которое собираются подключить к кондиционеру
        ConditionerEntity conditioner = conditionerDtoToEntity(conditionerDto);
        conditioner.setStart(false);
        conditioner.setDeleted(false);
        conditioner.setUuidConditioner(utils.createRandomUuid());
        String conditionerUuid = conditionerRepository.save(conditioner).getUuidConditioner();
        conditionerDto.setUuidConditioner(conditionerUuid);
        try {
            LOGGER.info("Conditioner created {}", mapper.writeValueAsString(conditioner));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ConditionerException(e.getMessage());
        }
        return conditionerDto;
    }

    public ConditionerDto getConditionerById(String conditionerUuid) {
        Optional<ConditionerEntity> optionalConditionerEntity = conditionerRepository.findByUuidConditionerAndDeleted(conditionerUuid, false);
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
            if (conditionerEntity.getStart()) {
                LOGGER.info(Messages.CHECK_UNIQUE_CONDITIONER + conditionerEntity.getInventoryNumber() +
                        Messages.WORKING_NOW);
                throw new ConditionerException(Messages.CHECK_UNIQUE_CONDITIONER + conditionerEntity.getInventoryNumber()
                        + Messages.WORKING_NOW);
            }
            conditionerEntity.setStart(true);
            conditionerEntity.setStartDate(new Date());
            conditionerRepository.save(conditionerEntity);
            LOGGER.info(Messages.CHECK_UNIQUE_CONDITIONER + conditionerEntity.getInventoryNumber() +
                    Messages.STARTED_WORK);
            return Messages.CHECK_UNIQUE_CONDITIONER + conditionerEntity.getInventoryNumber() +
                    Messages.STARTED_WORK;
        }
        LOGGER.error(Messages.CHECK_VALID_CONDITIONER + conditionerUuid + Messages.NOT_FOUND);
        throw new ConditionerException(Messages.CHECK_VALID_CONDITIONER + conditionerUuid + Messages.NOT_FOUND);
    }

    public ConditionerDto addTypeMaintenanceToConditioner(String conditionerUuid, String typeMaintenanceUuid) {
        Optional<ConditionerEntity> optionalConditionerEntity = conditionerRepository.findByUuidConditionerAndDeleted(
                conditionerUuid, false
        );
        if (!optionalConditionerEntity.isPresent()) {
            LOGGER.error(Messages.CHECK_VALID_CONDITIONER + conditionerUuid + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.CHECK_VALID_CONDITIONER + conditionerUuid + Messages.NOT_FOUND);
        }
        Optional<TypeMaintenanceEntity> optionalTypeMaintenanceEntity = typeMaintenanceRepository
                .findByUuidTypeMaintenanceAndDeleted(typeMaintenanceUuid, false);
        if (!optionalTypeMaintenanceEntity.isPresent()) {
            LOGGER.error(Messages.CHECK_UNIQUE_TYPE_MAINTENANCE + conditionerUuid + Messages.NOT_FOUND);
        }
        ConditionerEntity conditionerEntity = optionalConditionerEntity.get();
        List<TypeMaintenanceEntity> typeMaintenanceEntityList = new ArrayList<>();
        typeMaintenanceEntityList.add(optionalTypeMaintenanceEntity.get());
        conditionerEntity.setMaintenance(typeMaintenanceEntityList);
        conditionerRepository.save(conditionerEntity);
        ConditionerDto conditionerDto = conditionerToDto(conditionerEntity);
        LOGGER.info(Messages.CHECK_UNIQUE_TYPE_MAINTENANCE +
                optionalTypeMaintenanceEntity.get().getUuidTypeMaintenance() + Messages.ADDED +
                Messages.CHECK_UNIQUE_CONDITIONER + conditionerEntity.getUuidConditioner());
        return conditionerDto;
    }
}
