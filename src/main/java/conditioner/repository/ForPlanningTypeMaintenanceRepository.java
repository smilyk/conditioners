package conditioner.repository;


import conditioner.model.ForPlanningTypeMaintenanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface ForPlanningTypeMaintenanceRepository extends JpaRepository<ForPlanningTypeMaintenanceEntity, Long> {

    @Query(value = "SELECT * FROM conditioner.for_planning where next_type_maintenance_date <:now", nativeQuery = true)
    List<ForPlanningTypeMaintenanceEntity> getAllMissedTypeMaintenanceConditioners(LocalDateTime now);

    @Query(value = "SELECT * FROM conditioner.for_planning where next_type_maintenance_date " +
            "between :from and :to",
            nativeQuery = true)
    List<ForPlanningTypeMaintenanceEntity> getAllPlanningTypeMaintenanceConditioners(
            @Param("from") LocalDateTime startDate, @Param("to") LocalDateTime finishDate);
}
