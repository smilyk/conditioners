package conditioner.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import conditioner.dto.ConditionerDto;
import conditioner.exceptions.ConditionerException;
import conditioner.model.ConditionerEntity;
import conditioner.repository.ConditionerRepository;
import conditioner.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class ConditionerServiceImpl {
    @Autowired
    ConditionerRepository conditionerRepository;
    @Autowired
    Utils utils;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConditionerServiceImpl.class);
    private ObjectMapper mapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();


    public ConditionerDto createConditioner(ConditionerDto conditionerDto)  {
        ConditionerEntity conditioner = conditionerDtoToEntity(conditionerDto);
        conditioner.setStart(false);
        conditioner.setUuidConditioner(utils.createRandomUuid());
        String conditionerUuid = conditionerRepository.save(conditioner).getUuidConditioner();
        conditionerDto.setUuidConditioner(conditionerUuid);
        try {
            LOGGER.info("Conditioner {} created", mapper.writeValueAsString(conditioner));
        }catch (Exception e){
            throw new ConditionerException(e.getMessage());
        }
        return conditionerDto;
    }

    private ConditionerDto conditionerToDto(ConditionerEntity conditioner) {
        return modelMapper.map(conditioner, ConditionerDto.class);
    }

    private ConditionerEntity conditionerDtoToEntity(ConditionerDto conditionerDto) {
        return modelMapper.map(conditionerDto, ConditionerEntity.class);
    }
}
