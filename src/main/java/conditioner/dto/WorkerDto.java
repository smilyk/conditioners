package conditioner.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WorkerDto {

    private String userUuid;

    private String firstName;

    private String lastName;

    private String email;
}
