package conditioner.service;

import conditioner.dto.PlanningTypeMaintenanceConditioner;
import conditioner.dto.TypeMaintenanceForDto;
import conditioner.model.ForPlanningTypeMaintenanceEntity;
import conditioner.model.TypeMaintenanceEntity;
import conditioner.repository.ForPlanningTypeMaintenanceRepository;
import conditioner.repository.TypeMaintenanceRepository;
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
        return missedCond;
    }

    private PlanningTypeMaintenanceConditioner fromForPlanningToPlanning
            (ForPlanningTypeMaintenanceEntity forPlanningEntities) {
        Optional<TypeMaintenanceEntity> maintenance = typeMaintenanceRepository.findByUuidTypeMaintenanceAndDeleted(
                forPlanningEntities.getUuidTypeMaintenance(), false
        );
        if(!maintenance.isPresent()){
//            TODO error
        }
        TypeMaintenanceForDto maintenanceDto = maintenanceToDto(maintenance.get());
        PlanningTypeMaintenanceConditioner missedCond = getPlanningTypeMaintenanceConditioner(forPlanningEntities,
                maintenanceDto);
        return missedCond;
    }

    private PlanningTypeMaintenanceConditioner getPlanningTypeMaintenanceConditioner(
            ForPlanningTypeMaintenanceEntity forPlanningEntities, TypeMaintenanceForDto maintenanceDto) {
        return PlanningTypeMaintenanceConditioner.builder()
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
