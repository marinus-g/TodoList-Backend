package academy.mischok.todoapp.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    protected Long id;
    @Column(name = "user_name")
    protected String userName;
    @Column(name = "email")
    protected String email;
    @Column(name = "password")
    protected String password;
}
