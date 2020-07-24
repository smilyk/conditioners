package conditioner.repository;

import conditioner.model.ConditionerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConditionerRepository extends JpaRepository<ConditionerEntity, Long> {

    Optional<ConditionerEntity> findByInventoryNumber(String inventoryNumber);

    Optional<ConditionerEntity>findByUuidConditioner(String conditionerUuid);

    Optional<ConditionerEntity> findByUuidConditionerAndDeleted(String conditionerUuid, boolean deleted);
}
