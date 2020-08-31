package conditioner.repository;

import conditioner.enums.RoleEnum;
import conditioner.model.ArchiveEntity;
import conditioner.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArchiveRepository extends JpaRepository<ArchiveEntity, Long> {

}
