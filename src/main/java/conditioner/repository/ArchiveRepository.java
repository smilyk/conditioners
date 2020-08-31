package conditioner.repository;

import conditioner.model.ArchiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveRepository extends JpaRepository<ArchiveEntity, Long> {

}
