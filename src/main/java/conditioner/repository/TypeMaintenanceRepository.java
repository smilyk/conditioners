package conditioner.repository;


import conditioner.model.TypeMaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TypeMaintenanceRepository extends JpaRepository<TypeMaintenanceEntity, Long> {

    Optional<TypeMaintenanceEntity> findByNameMaintenanceAndPeopleHours(String nameMaintenance, String peopleHours);

    Optional<TypeMaintenanceEntity> findByUuidTypeMaintenanceAndDeleted(String typeMaintenanceUuid, boolean b);

    Optional<TypeMaintenanceEntity> findByUuidTypeMaintenance(String typeMaintenanceUuid);
}
