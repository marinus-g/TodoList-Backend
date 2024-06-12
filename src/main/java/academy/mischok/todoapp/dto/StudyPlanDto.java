package academy.mischok.todoapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Set;


@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyPlanDto {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    @JsonProperty("todos")
    private Set<ToDoDto> todos;
    @JsonProperty("owner_id")
    private Long ownerId;
}

