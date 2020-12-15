//package conditioner.model;
//
//import lombok.*;
//import lombok.experimental.SuperBuilder;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@SuperBuilder
//@Entity(name = "Pictures")
//@Table(name="pictures")
//public class PicturesEntity extends BaseEntity implements Serializable {
//    private String uuidPicture;
//    @Column(nullable = false)
//    String pictureName;
//    @Column
//    @ManyToMany(mappedBy = "picture", fetch = FetchType.EAGER)
//    private List<ArticleEntity> articles = new ArrayList<>();
//    String pictureUrl;
//}
