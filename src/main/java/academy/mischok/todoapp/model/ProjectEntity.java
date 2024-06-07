package academy.mischok.todoapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ToDoEntity> todo = new ArrayList<>();

    @OneToMany(mappedBy = "projectEntity", orphanRemoval = true)
    private Set<ToDoEntity> toDoEntities = new LinkedHashSet<>();

}