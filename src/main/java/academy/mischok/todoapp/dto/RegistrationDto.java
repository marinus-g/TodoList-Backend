package academy.mischok.todoapp.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RegistrationDto {

    @Nullable
    private String username;
    @Email(message = "Invalid email address")
    private String email;
    @Length(min = 8, max = 50)
    private String password;

}