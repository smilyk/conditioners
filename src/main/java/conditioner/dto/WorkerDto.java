package conditioner.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WorkerDto {

    String userUuid;

    private String firstName;

    private String lastName;

    private String email;
}
