package academy.mischok.todoapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UserDto {

    private Long id;
    @NotBlank
    private String userName;
    @Email(message = "Invalid email address")
    private String email;
}
