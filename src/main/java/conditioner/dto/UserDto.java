package conditioner.dto;

import conditioner.enums.RoleEnum;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

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
