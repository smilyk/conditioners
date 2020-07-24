package conditioner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import conditioner.constants.Messages;
import conditioner.dto.ConditionerDto;
import conditioner.exceptions.ConditionerException;
import conditioner.model.ConditionerEntity;
import conditioner.repository.ConditionerRepository;
import conditioner.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ConditionerServiceImpl {
    @Autowired
    ConditionerRepository conditionerRepository;
    @Autowired
    Utils utils;
    private static final Logger LOGGER = LogManager.getLogger(ConditionerServiceImpl.class);

    private ObjectMapper mapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();


    public ConditionerDto createConditioner(ConditionerDto conditionerDto)  {
        ConditionerEntity conditioner = conditionerDtoToEntity(conditionerDto);
        conditioner.setStart(false);
        conditioner.setDeleted(false);
        conditioner.setUuidConditioner(utils.createRandomUuid());
        String conditionerUuid = conditionerRepository.save(conditioner).getUuidConditioner();
        conditionerDto.setUuidConditioner(conditionerUuid);
        try {
            LOGGER.info("Conditioner created {}", mapper.writeValueAsString(conditioner));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new ConditionerException(e.getMessage());
        }
        return conditionerDto;
    }

    public ConditionerDto getConditionerById(String conditionerUuid) {
        Optional<ConditionerEntity> optionalConditionerEntity = conditionerRepository.findByUuidConditionerAndDeleted(conditionerUuid, false);
        if(!optionalConditionerEntity.isPresent()){
            LOGGER.error(Messages.CONDITIONER + Messages.WITH_ID + conditionerUuid + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.CONDITIONER + Messages.WITH_ID + conditionerUuid + Messages.NOT_FOUND);
        }
        ConditionerEntity conditioner = optionalConditionerEntity.get();
        ConditionerDto conditionerDto = conditionerToDto(conditioner);
        try {
            LOGGER.info("Conditioner found {}", mapper.writeValueAsString(conditioner));
        }catch (Exception e){
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
        if(!optionalConditionerEntity.isPresent()){
            LOGGER.error(Messages.CONDITIONER + Messages.WITH_ID + conditionerUuid + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.CONDITIONER + Messages.WITH_ID + conditionerUuid + Messages.NOT_FOUND);
        }
        ConditionerEntity conditioner = optionalConditionerEntity.get();
        conditioner.setDeleted(true);
        conditionerRepository.save(conditioner);
        ConditionerDto conditionerDto = conditionerToDto(conditioner);
        try {
            LOGGER.info("Conditioner deleted {}", mapper.writeValueAsString(conditioner));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new ConditionerException(e.getMessage());
        }
        return conditionerDto;
    }
}
