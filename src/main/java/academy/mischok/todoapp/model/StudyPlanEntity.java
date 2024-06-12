package academy.mischok.todoapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.HashSet;
import  java.util.List;
import java.util.Set;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToMany(mappedBy = "studyplans")
    private Set<ToDoEntity> todos = new HashSet<>();
}

