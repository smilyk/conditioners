package conditioner.model;

import conditioner.dto.PicturesEntityDto;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "Article")
@Table(name="article")
public class ArticleEntity extends BaseEntity implements Serializable {
    @Column(nullable = false)
    private String uuidArticle;

    @Column(nullable = false)
    private String articleName;

    @Column(nullable = false)
    private String articleText;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    }, fetch=FetchType.EAGER)
    @JoinTable(name = "article_pictures",
            joinColumns = @JoinColumn(name = "uuid_article"),
            inverseJoinColumns = @JoinColumn(name = "pictures_uuid")
    )
    private List<PicturesEntity> picture = new ArrayList<>();
}
