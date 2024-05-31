package academy.mischok.todoapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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


    @Length(min = 3, max = 50)

    private String username;

    @Email(message = "Invalid email address")
    private String email;
    @Length(min = 8, max = 50)
    private String password;

}