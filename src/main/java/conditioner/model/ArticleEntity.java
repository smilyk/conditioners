package conditioner.model;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@Entity(name = "Article")
@Table(name="article")
public class ArticleEntity extends BaseEntity implements Serializable {
    @Column(nullable = false)
    private String uuidArticle;

    @Column(nullable = false)
    private String articleName;

    @Column(nullable = false)
    private String articleTitle;

    @Column(nullable = false)
    private String articleText;

    @Column
    private String pictureName;

    @Column
    private String pictureUrl;


}
