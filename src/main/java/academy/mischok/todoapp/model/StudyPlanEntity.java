package academy.mischok.todoapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class StudyPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "study_plan_id")
    private Long id;
    @Column(name = "study_plan_title")
    private String title;
    @Column(name = "study_plan_start_date")
    private Date startDate;
    @Column(name = "study_plan_end_date")
    private Date endDate;
    @ManyToOne
    private UserEntity owner;

}
