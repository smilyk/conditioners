package conditioner.service;

import conditioner.constants.Messages;
import conditioner.dto.ConditionerDto;
import conditioner.exceptions.ConditionerException;
import conditioner.model.ConditionerEntity;
import conditioner.repository.ConditionerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidationService {
    @Autowired
    ConditionerRepository conditionerRepository;

    public void validConditioner(ConditionerDto conditionerDto) {
        String field = "Name of conditioner";
        if(conditionerDto.getNameConditioner().equals("")){
            throw new ConditionerException(field + Messages.CAN_NOT_BE_EMPTY);
        }
        field = " Inventory number";
        if (conditionerDto.getInventoryNumber().equals("")){
            throw new ConditionerException(field + Messages.CAN_NOT_BE_EMPTY);
        }
        field = "Place";
        if (conditionerDto.getPlace().equals("")){
            throw new ConditionerException(field + Messages.CAN_NOT_BE_EMPTY);
        }
    }

    public void validUniqConditioner(String inventoryNumber) {
        Optional<ConditionerEntity> entity = conditionerRepository.findByInventoryNumber(inventoryNumber);
        if(entity.isPresent()){
            throw new ConditionerException(Messages.CHECK_UNIQUE_CONDITIONER + inventoryNumber + Messages.EXISTS);
        }
    }
}
