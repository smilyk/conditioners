package conditioner.repository;


import conditioner.model.TypeMaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;


public interface TypeMaintenanceRepository extends JpaRepository<TypeMaintenanceEntity, Long> {

    Optional<TypeMaintenanceEntity> findByNameMaintenanceAndPeopleHours(String nameMaintenance, @NotNull(message = "field peopleHours id required") Integer peopleHours);

    Optional<TypeMaintenanceEntity> findByUuidTypeMaintenanceAndDeleted(String typeMaintenanceUuid, boolean b);

    Optional<TypeMaintenanceEntity> findByUuidTypeMaintenance(String typeMaintenanceUuid);

    List<TypeMaintenanceEntity> findAllByDeleted(boolean b);
}
