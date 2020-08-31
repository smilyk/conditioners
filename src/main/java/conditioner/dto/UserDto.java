package conditioner.dto;

import conditioner.enums.RoleEnum;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

    String userUuid;

    private String firstName;

    private String lastName;

    private String email;

    private RoleEnum role;

    private Boolean deleted;
}
