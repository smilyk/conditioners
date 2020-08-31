package conditioner.model;

import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @PrePersist
    public void onPersist() {
        final DateTime nowDt = new DateTime(DateTimeZone.UTC);
        final LocalDateTime current = LocalDateTime.now();

        setCreateDate(current);
        setUpdateDate(current);
    }
    @PreUpdate
    public void onUpdate() {
        setUpdateDate(LocalDateTime.now());
    }
}
