package academy.mischok.todoapp.dto;

import lombok.*;

import java.util.List;


@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudyPlanDto {
    private Long id;
    private String title;
    private String startDate;
    private String endDate;
    private List<Long> todos;
    private Long ownerId;
}
