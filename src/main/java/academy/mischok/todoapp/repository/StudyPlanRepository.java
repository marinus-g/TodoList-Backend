package academy.mischok.todoapp.repository;

import academy.mischok.todoapp.model.StudyPlanEntity;
import academy.mischok.todoapp.model.ToDoEntity;
import academy.mischok.todoapp.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudyPlanRepository extends JpaRepository<StudyPlanEntity, Long> {
    boolean existsByTitle(String title);
    Optional<StudyPlanEntity> findByTitle(String title);
    Optional<StudyPlanEntity> findByIdAndUser(long id, UserEntity user);
}
