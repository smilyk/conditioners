package conditioner.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class PicturesEntityDto {
    @NotNull(message = "field pictureName id required")
    String pictureName;
    String uuidPicture;
    String pictureUrl;
    @NotNull(message = "field pictures id required")
    String pictures;
}
