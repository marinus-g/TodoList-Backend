package academy.mischok.todoapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

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
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
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
    @JoinColumn(name = "project_entity_project_id")
    private ProjectEntity projectEntity;

}