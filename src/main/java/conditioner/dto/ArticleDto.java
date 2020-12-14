package conditioner.dto;

import conditioner.model.PicturesEntity;
import conditioner.model.TypeMaintenanceEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @NotNull(message = "field articleText id required")
    @Column(nullable = false)
    private String articleText;
    @NotNull(message = "field pictureUrl id required")
    @Column(nullable = false)
    List<PicturesEntityDto> pictures = new ArrayList<>();


}
