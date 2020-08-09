package conditioner.repository;

import conditioner.model.InWorkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InWorkRepository extends JpaRepository<InWorkEntity, Long> {

}
