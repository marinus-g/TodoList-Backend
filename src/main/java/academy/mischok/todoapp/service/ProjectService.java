package academy.mischok.todoapp.service;

import academy.mischok.todoapp.dto.ProjectDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.service.impl.ProjectNotFoundException;
import academy.mischok.todoapp.service.impl.UnauthorizedException;
import academy.mischok.todoapp.validation.ProjectValidation;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    ProjectValidation isTitleValid(String title);

    Optional<ProjectDto> createProject(ProjectDto projectDto);

    List<ProjectDto> findAllProject(UserEntity user);
    List<ProjectDto> findProject(UserEntity user);
    Optional<ProjectDto> updateProject(Long id, ProjectDto projectDto);

    ProjectDto findProjectByIdAndUser(Long id, UserEntity user) throws ProjectNotFoundException;
    void addUserToProject(Long projectId, Long userId) throws ProjectNotFoundException;
    void removeUserFromProject (Long projectId, Long userId) throws ProjectNotFoundException;
    Optional<ProjectDto> addUserToProject(Long projectId, Long userId, UserEntity requestingUser) throws ProjectNotFoundException, UnauthorizedException;

    Optional<ProjectDto> removeUserFromProject(Long projectId, Long userId, UserEntity requestingUser) throws ProjectNotFoundException, UnauthorizedException;
}
