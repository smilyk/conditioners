package conditioner.service;

import conditioner.constants.Messages;
import conditioner.dto.ConditionerDto;
import conditioner.dto.TypeMaintenanceDto;
import conditioner.exceptions.ConditionerException;
import conditioner.model.ConditionerEntity;
import conditioner.model.TypeMaintenanceEntity;
import conditioner.repository.ConditionerRepository;
import conditioner.repository.TypeMaintenanceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidationService {
    private static final Logger LOGGER = LogManager.getLogger(ConditionerServiceImpl.class);


    @Autowired
    ConditionerRepository conditionerRepository;
    @Autowired
    TypeMaintenanceRepository typeMaintenanceRepository;

    public void validConditioner(ConditionerDto conditionerDto) {
        String field = "Name of conditioner";
        if(conditionerDto.getNameConditioner().equals("")){
            LOGGER.error(field + Messages.CAN_NOT_BE_EMPTY);
            throw new ConditionerException(field + Messages.CAN_NOT_BE_EMPTY);
        }
        field = " Inventory number";
        if (conditionerDto.getInventoryNumber().equals("")){
            LOGGER.error(field + Messages.CAN_NOT_BE_EMPTY);
            throw new ConditionerException(field + Messages.CAN_NOT_BE_EMPTY);
        }
        field = "Place";
        if (conditionerDto.getPlace().equals("")){
            LOGGER.error(field + Messages.CAN_NOT_BE_EMPTY);
            throw new ConditionerException(field + Messages.CAN_NOT_BE_EMPTY);
        }
    }

    public void validUniqConditioner(String inventoryNumber) {
        Optional<ConditionerEntity> entity = conditionerRepository.findByInventoryNumber(inventoryNumber);
        if(entity.isPresent()){
            LOGGER.error(Messages.CHECK_UNIQUE_CONDITIONER + inventoryNumber + Messages.EXISTS);
            throw new ConditionerException(Messages.CHECK_UNIQUE_CONDITIONER + inventoryNumber + Messages.EXISTS);
        }
    }


    public void validTypeMaintenance(TypeMaintenanceDto typeMaintenanceDto) {
        Optional<TypeMaintenanceEntity> typeMaintenanceEntity = typeMaintenanceRepository
                .findByNameMaintenanceAndPeopleHours(
                typeMaintenanceDto.getNameMaintenance(), typeMaintenanceDto.getPeopleHours()
        );
        if(typeMaintenanceEntity.isPresent()){
            LOGGER.error(Messages.CHECK_UNIQUE_TYPE_MAINTENANCE + typeMaintenanceDto.getNameMaintenance()
            + Messages.EXISTS);
            throw new ConditionerException(Messages.CHECK_UNIQUE_TYPE_MAINTENANCE + typeMaintenanceDto.getNameMaintenance()
                    + Messages.EXISTS);

        }
    }
}
