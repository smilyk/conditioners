package conditioner.repository;

import conditioner.enums.RoleEnum;
import conditioner.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByRole(RoleEnum role);

    Optional<UserEntity> findByUserUuid(String workersUuid);
}
