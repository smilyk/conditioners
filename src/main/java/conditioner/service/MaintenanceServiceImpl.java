package conditioner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import conditioner.constants.Messages;
import conditioner.dto.TypeMaintenanceDto;
import conditioner.exceptions.ConditionerException;
import conditioner.model.TypeMaintenanceEntity;
import conditioner.repository.TypeMaintenanceRepository;
import conditioner.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaintenanceServiceImpl {

    private static final Logger LOGGER = LogManager.getLogger(ConditionerServiceImpl.class);

    private ObjectMapper mapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();
    @Autowired
    TypeMaintenanceRepository typeMaintenanceRepository;
    @Autowired
    Utils utils;

    public TypeMaintenanceDto createTypeMaintenance(TypeMaintenanceDto typeMaintenanceDto) {
        TypeMaintenanceEntity typeMaintenance = typeMaintenanceToEntity(typeMaintenanceDto);
       typeMaintenance.setDeleted(false);
        typeMaintenance.setUuidTypeMaintenance(utils.createRandomUuid());
        String typeMaintenanceUuid = typeMaintenanceRepository.save(typeMaintenance).getUuidTypeMaintenance();
        typeMaintenanceDto.setUuidTypeMaintenance(typeMaintenanceUuid);
        try {
            LOGGER.info("TypeMaintenance created {}", mapper.writeValueAsString(typeMaintenance));
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new ConditionerException(e.getMessage());
        }
        return typeMaintenanceDto;
    }

    private TypeMaintenanceEntity typeMaintenanceToEntity(TypeMaintenanceDto typeMaintenanceDto) {
        return modelMapper.map(typeMaintenanceDto, TypeMaintenanceEntity.class);
    }
    private TypeMaintenanceDto typeMaintenanceToDto(TypeMaintenanceEntity typeMaintenance) {
        return modelMapper.map(typeMaintenance, TypeMaintenanceDto.class);
    }


    public TypeMaintenanceDto getTypeMaintenanceById(String typeMaintenanceUuid) {
        Optional<TypeMaintenanceEntity> optionalTypeMaintenanceEntity = typeMaintenanceRepository
                .findByUuidTypeMaintenanceAndDeleted(typeMaintenanceUuid, false);
        if(!optionalTypeMaintenanceEntity.isPresent()){
            LOGGER.error(Messages.CONDITIONER + Messages.WITH_ID + typeMaintenanceUuid + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.CONDITIONER + Messages.WITH_ID + typeMaintenanceUuid + Messages.NOT_FOUND);
        }
        TypeMaintenanceEntity typeMaintenance = optionalTypeMaintenanceEntity.get();
        TypeMaintenanceDto typeMaintenanceDto = typeMaintenanceToDto(typeMaintenance);
        try {
            LOGGER.info("TypeMaintenance with id {} found", typeMaintenanceUuid);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new ConditionerException(e.getMessage());
        }
        return typeMaintenanceDto;
    }



    public List<TypeMaintenanceDto> getAllTypeMaintenances() {
        List<TypeMaintenanceEntity> typeMaintenanceEntities = typeMaintenanceRepository.findAll();
        List<TypeMaintenanceDto> typeMaintenanceDtos = typeMaintenanceEntities.stream()
                .map(this::typeMaintenanceToDto).collect(Collectors.toList());
        LOGGER.info(Messages.LIST + Messages.TYPE_MAINTENANCE + Messages.FOUND);
        return typeMaintenanceDtos;

    }

    public TypeMaintenanceDto deleteTypeMaintenance(String typeMaintenanceUuid) {
         Optional<TypeMaintenanceEntity> optionalTypeMaintenanceEntity =
                 typeMaintenanceRepository.findByUuidTypeMaintenance(typeMaintenanceUuid);
        if(!optionalTypeMaintenanceEntity.isPresent()){
            LOGGER.error(Messages.TYPE_MAINTENANCE + Messages.WITH_ID + typeMaintenanceUuid + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.TYPE_MAINTENANCE + Messages.WITH_ID
                    + typeMaintenanceUuid + Messages.NOT_FOUND);
        }
        TypeMaintenanceEntity typeMaintenanceEntity = optionalTypeMaintenanceEntity.get();
        typeMaintenanceEntity.setDeleted(true);
        typeMaintenanceRepository.save(typeMaintenanceEntity);
        TypeMaintenanceDto typeMaintenanceDto = typeMaintenanceToDto(typeMaintenanceEntity);
        try {
            LOGGER.info("Type maintenance with id {} deleted ", typeMaintenanceUuid);
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new ConditionerException(e.getMessage());
        }
        return typeMaintenanceDto;
    }
}
