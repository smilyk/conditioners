package conditioner.service;

import conditioner.constants.Messages;
import conditioner.dto.DatesForPlanningDto;
import conditioner.dto.PlanningTypeMaintenanceConditioner;
import conditioner.dto.TypeMaintenanceForDto;
import conditioner.exceptions.ConditionerException;
import conditioner.model.ForPlanningTypeMaintenanceEntity;
import conditioner.model.TypeMaintenanceEntity;
import conditioner.repository.ForPlanningTypeMaintenanceRepository;
import conditioner.repository.TypeMaintenanceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlanningService {
    private ModelMapper modelMapper = new ModelMapper();
    private static final Logger LOGGER = LogManager.getLogger(ConditionerServiceImpl.class);

    @Autowired
    ForPlanningTypeMaintenanceRepository forPlanningTypeMaintenanceRepository;

    @Autowired
    TypeMaintenanceRepository typeMaintenanceRepository;

    public List<PlanningTypeMaintenanceConditioner> getAllConditionerWithMissedTypeMaintenance() {

        LocalDateTime now = LocalDateTime.now();
        List<ForPlanningTypeMaintenanceEntity> forPlanningEntities =
                forPlanningTypeMaintenanceRepository.getAllMissedTypeMaintenanceConditioners(now);
        List<PlanningTypeMaintenanceConditioner> missedCond = forPlanningEntities.stream().map(this::fromForPlanningToPlanning)
                .collect(Collectors.toList());
        LOGGER.info(Messages.LIST + Messages.CONDITIONER + Messages.MISSED_TYPE + Messages.FOUND);
        return missedCond;
    }

    public List<PlanningTypeMaintenanceConditioner> getAllForPlanning(DatesForPlanningDto dates) {
        List<ForPlanningTypeMaintenanceEntity> forPlanningEntities =
                forPlanningTypeMaintenanceRepository.getAllPlanningTypeMaintenanceConditioners(dates.getStartDate(),
                        dates.getFinishDate());
        List<PlanningTypeMaintenanceConditioner> planningCond = forPlanningEntities.stream().map(this::fromForPlanningToPlanning)
                .collect(Collectors.toList());
        LOGGER.info(Messages.LIST + Messages.FOR_PLANNING + Messages.FOUND);

        return planningCond;
    }

    private PlanningTypeMaintenanceConditioner fromForPlanningToPlanning
            (ForPlanningTypeMaintenanceEntity forPlanningEntities) {
        Optional<TypeMaintenanceEntity> maintenance = typeMaintenanceRepository.findByUuidTypeMaintenanceAndDeleted(
                forPlanningEntities.getUuidTypeMaintenance(), false
        );
        if (!maintenance.isPresent()) {
            LOGGER.error(Messages.TYPE_MAINTENANCE + Messages.WITH_ID + forPlanningEntities.getUuidTypeMaintenance()
                    + Messages.NOT_FOUND);
            throw new ConditionerException(Messages.TYPE_MAINTENANCE + Messages.WITH_ID + forPlanningEntities.getUuidTypeMaintenance()
                    + Messages.NOT_FOUND);
        }
        TypeMaintenanceForDto maintenanceDto = maintenanceToDto(maintenance.get());
        PlanningTypeMaintenanceConditioner missedCond = getPlanningTypeMaintenanceConditioner(forPlanningEntities,
                maintenanceDto);
        return missedCond;
    }

    private PlanningTypeMaintenanceConditioner getPlanningTypeMaintenanceConditioner(
            ForPlanningTypeMaintenanceEntity forPlanningEntities, TypeMaintenanceForDto maintenanceDto) {
        return PlanningTypeMaintenanceConditioner.builder()
                .uuidRecords(forPlanningEntities.getUuidRecord())
                .uuidConditioner(forPlanningEntities.getUuidConditioner())
                .inventoryNumber(forPlanningEntities.getInventoryNumber())
                .nameConditioner(forPlanningEntities.getNameConditioner())
                .maintenance(maintenanceDto)
                .lastTypeMaintenanceDate(forPlanningEntities.getLastTypeMaintenanceDate())
                .nextTypeMaintenanceDate(forPlanningEntities.getNextTypeMaintenanceDate())
                .place(forPlanningEntities.getPlace())
                .build();
    }

    private TypeMaintenanceForDto maintenanceToDto(TypeMaintenanceEntity typeMaintenance) {
        return modelMapper.map(typeMaintenance, TypeMaintenanceForDto.class);
    }


}
