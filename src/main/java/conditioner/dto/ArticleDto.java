package conditioner.dto;


import lombok.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ArticleDto {

    private String uuidArticle;
    @NotNull(message = "field articleName id required")
    private String articleName;

    @NotNull(message = "field articleTitle id required")
    private String articleTitle;
    @NotNull(message = "field articleText id required")
    private String articleText;

    private String pictureName;

    private String pictureUrl;

    private String pictureBody;

//    @NotNull(message = "field pictureUrl id required")
//    @Column(nullable = false)
//    List<PicturesEntityDto> picture = new ArrayList<>();

}
