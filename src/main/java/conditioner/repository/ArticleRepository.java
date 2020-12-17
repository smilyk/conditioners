package conditioner.repository;

import conditioner.model.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

    Optional<ArticleEntity> findByUuidArticle(String articleUuid);

    Optional<ArticleEntity> findBypictureName(String pictureName);
}
