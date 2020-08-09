package conditioner.repository;

import conditioner.model.BusyWorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusyWorkersRepository extends JpaRepository<BusyWorkerEntity, Long> {

}
