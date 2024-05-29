package academy.mischok.todoapp.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected long id;
    @Column(name = "userName")
    protected String userName;
    @Column(name = "email")
    protected String email;
    @Column(name = "password")
    protected String password;
}
