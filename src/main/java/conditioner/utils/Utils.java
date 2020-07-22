package conditioner.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class Utils {

    public String createRandomUuid(){
        return UUID.randomUUID().toString();
    }
}
