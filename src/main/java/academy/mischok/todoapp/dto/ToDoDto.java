package academy.mischok.todoapp.dto;

import academy.mischok.todoapp.model.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.*;


import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDto {
    private Long id;
    @NotBlank
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Status status;
}
