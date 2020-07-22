package conditioner.model;
import lombok.Data;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Date createDate;

    private Date updateDate;

    @PrePersist
    public void onPersist() {
        final DateTime nowDt = new DateTime(DateTimeZone.UTC);
        final Date current = new Date(nowDt.getMillis());
        setCreateDate(current);
        setUpdateDate(current);
    }
    @PreUpdate
    public void onUpdate() {
        setUpdateDate(new Date(new DateTime(DateTimeZone.UTC).getMillis()));
    }
}
