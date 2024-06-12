package academy.mischok.todoapp.repository;

import academy.mischok.todoapp.model.ProjectEntity;
import academy.mischok.todoapp.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    List<ProjectEntity> findByOwner(UserEntity owner);
    Optional<ProjectEntity> findByIdAndOwner(Long id, UserEntity user);

}
