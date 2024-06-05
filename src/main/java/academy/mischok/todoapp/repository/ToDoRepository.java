package academy.mischok.todoapp.repository;

import academy.mischok.todoapp.model.ToDoEntity;
import academy.mischok.todoapp.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToDoRepository extends JpaRepository<ToDoEntity, Long> {
    boolean existsByTitle(String title);
    Optional<ToDoEntity> findByTitle(String title);
    List<ToDoEntity> findByUser(UserEntity user);
    Optional<ToDoEntity> findByIdAndUser(long id, UserEntity user);

}
