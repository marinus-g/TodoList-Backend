package academy.mischok.todoapp.dto;


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
    private String title;
    private String description;
    private Long ownerId;
    private List<Long> todos;

}