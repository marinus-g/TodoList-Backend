package academy.mischok.todoapp.repository;

import academy.mischok.todoapp.model.ProjectEntity;
import academy.mischok.todoapp.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByTitleContainingIgnoreCase(String title);
    List<ProjectEntity> findByOwner(UserEntity owner);
    List<ProjectEntity> findByTitleContainingIgnoreCaseAndOwner(String title, UserEntity owner);
}
