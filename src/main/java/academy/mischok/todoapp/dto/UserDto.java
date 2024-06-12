package academy.mischok.todoapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserDto {

    private Long id;
    @NotBlank
    private String username;
    @Email(message = "Invalid email address")
    private String email;
    private String password;
    private Set<ProjectDto> projects;
    private Set<StudyPlanDto> studyplans;
    private List<ToDoDto> todos;

}