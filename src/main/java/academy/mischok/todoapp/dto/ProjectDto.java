package academy.mischok.todoapp.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private Long ownerId;
    private List<Long> todos;

}