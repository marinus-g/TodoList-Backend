package academy.mischok.todoapp.service.impl;

import academy.mischok.todoapp.converter.impl.ProjectEntityConverter;
import academy.mischok.todoapp.dto.ProjectDto;
import academy.mischok.todoapp.exception.ProjectNotFoundException;
import academy.mischok.todoapp.exception.UnauthorizedException;
import academy.mischok.todoapp.model.ProjectEntity;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.repository.ProjectRepository;
import academy.mischok.todoapp.repository.UserRepository;
import academy.mischok.todoapp.service.ProjectService;
import academy.mischok.todoapp.service.UserService;
import academy.mischok.todoapp.validation.ProjectValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectEntityConverter projectEntityConverter;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public ProjectValidation isTitleValid(String title) {
        if (title == null) {
            return new ProjectValidation(false, "Title cannot be null");
        } else if (title.isBlank()) {
            return new ProjectValidation(false, "Title cannot be blank");
        }
        return new ProjectValidation(true, null);
    }

    @Override
    public Optional<ProjectDto> createProject(ProjectDto projectDto) {
        return Optional.ofNullable(projectDto)
                .map(projectEntityConverter::convertToEntity)
                .map(projectRepository::save)
                .map(projectEntityConverter::convertToDto);
    }

    @Override
    public List<ProjectDto> findAllProject(UserEntity user) {
        return this.projectRepository.findByOwner(user)
                .stream()
                .map(projectEntityConverter::convertToDto)
                .toList();
    }

    @Override
    public List<ProjectDto> findProject(UserEntity user) {
        return List.of();
    }

    @Override
    public Optional<ProjectDto> updateProject(Long id, ProjectDto projectDto) {
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(id);
        return optionalProjectEntity.map(projectEntity -> {
                    projectEntity.setTitle(projectDto.getTitle());
                    projectEntity.setDescription(projectDto.getDescription());
                    return projectRepository.save(projectEntity);
                })
                .map(projectEntityConverter::convertToDto);
    }

    @Override
    public ProjectDto findProjectByIdAndUser(Long id, UserEntity user) throws ProjectNotFoundException {
        Optional<ProjectEntity> projectEntity = projectRepository.findByIdAndOwner(id, user);
        if (projectEntity.isPresent()) {
            return projectEntityConverter.convertToDto(projectEntity.get());
        } else {
            throw new ProjectNotFoundException("Project not found for the given id and user.");
        }
    }

    @Override
    public void addUserToProject(Long projectId, Long userId) throws ProjectNotFoundException {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
        UserEntity user = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        project.getUsers().add(user);
        projectRepository.save(project);
    }

    @Override
    public void removeUserFromProject(Long projectId, Long userId) throws ProjectNotFoundException {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));
        UserEntity user = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        project.getUsers().remove(user);
        projectRepository.save(project);
    }

    @Override
    public Optional<ProjectDto> addUserToProject(Long projectId, Long userId, UserEntity requestingUser) throws ProjectNotFoundException, UnauthorizedException {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        if (!project.getOwnerId().equals(requestingUser.getId())) {
            throw new UnauthorizedException("You are not authorized to add users to this project");
        }

        UserEntity userToAdd = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        project.getUsers().add(userToAdd);
        projectRepository.save(project);

        return Optional.of(projectEntityConverter.convertToDto(project));
    }

    @Override
    public Optional<ProjectDto> removeUserFromProject(Long projectId, Long userId, UserEntity requestingUser) throws ProjectNotFoundException, UnauthorizedException {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + projectId));

        if (!project.getOwnerId().equals(requestingUser.getId())) {
            throw new UnauthorizedException("You are not authorized to remove users from this project");
        }

        UserEntity userToRemove = userService.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        project.getUsers().remove(userToRemove);
        projectRepository.save(project);

        return Optional.of(projectEntityConverter.convertToDto(project));
    }
}