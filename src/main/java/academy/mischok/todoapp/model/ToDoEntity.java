package academy.mischok.todoapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ToDoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;


    @Column(name = "title")
    private java.lang.String title;
    @Column(name = "description", columnDefinition = "TEXT")
    private java.lang.String description;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Enumerated
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToMany
    @JoinTable(
            name = "TodoStudyplanTimeframeEntity",
            joinColumns = @JoinColumn(name = "todo_id"),
            inverseJoinColumns = @JoinColumn(name = "studyplan_id")
    )
    private Set<StudyPlanEntity> studyplans = new HashSet<>();
}
