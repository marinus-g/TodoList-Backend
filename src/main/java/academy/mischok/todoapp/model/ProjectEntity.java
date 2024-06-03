package academy.mischok.todoapp.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_id")
    private Long id;
    @Column(name = "project_title")
    private String title;
    @Column(name = "project_description")
    private String description;
    @ManyToOne
    private UserEntity owner;

}