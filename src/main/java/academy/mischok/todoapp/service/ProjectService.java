package academy.mischok.todoapp.service;

import academy.mischok.todoapp.dto.ProjectDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.validation.ProjectValidation;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    ProjectValidation isTitleValid(String title);

    Optional<ProjectDto> createProject(ProjectDto projectDto);

    List<ProjectDto> findAllProject(UserEntity user);
    List<ProjectDto> findProject(UserEntity user);
    Optional<ProjectDto> updateProject(Long id, ProjectDto projectDto);
}
