package academy.mischok.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ToDoDto {
    private Long id;
    @NotBlank
    private String title;
    private String description;
    private Timestamp startDate;
    private Timestamp endDate;
}
