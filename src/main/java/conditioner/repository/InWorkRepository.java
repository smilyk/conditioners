package conditioner.repository;

import conditioner.model.InWorkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InWorkRepository extends JpaRepository<InWorkEntity, Long> {

    @Query(value = "SELECT * FROM in_work where first_worker_uuid=:workerUuid or\n" +
            "second_worker_uuid=:workerUuid and start_time <:now " +
            " and in_work=:inWork", nativeQuery = true)
    List<InWorkEntity> findByWorkersUuid(@Param("workerUuid") String workersUuid, @Param("now") LocalDateTime now,
                                         @Param("inWork") boolean inWork);


    @Query(value = "SELECT * FROM in_work where first_worker_uuid=:workerUuid or " +
            "second_worker_uuid=:workerUuid and start_time " +
            "between :from and :to", nativeQuery = true)
    List<InWorkEntity> findByWorkersUuidAndDate(@Param("workerUuid") String workerUuid,
                                                @Param("from") LocalDateTime startDate,
                                                @Param("to") LocalDateTime finishDate);

    @Query(value = "SELECT * FROM in_work where first_worker_uuid=:workerUuid or\n" +
            "second_worker_uuid=:workerUuid and records_uuid=:recordUuid", nativeQuery = true)
    InWorkEntity findByWorkersUuidAndRecordsUuid(@Param("workerUuid")String workerUuid,
                                                 @Param("recordUuid")String recordUuid);
}
