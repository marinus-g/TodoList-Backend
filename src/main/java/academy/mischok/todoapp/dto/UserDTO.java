package academy.mischok.todoapp.dto;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UserDTO {

    private Long id;
    private String userName;
    private String email;
}
