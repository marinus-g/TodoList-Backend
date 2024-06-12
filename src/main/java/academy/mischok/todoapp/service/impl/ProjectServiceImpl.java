package academy.mischok.todoapp.service.impl;

import academy.mischok.todoapp.converter.impl.ProjectEntityConverter;
import academy.mischok.todoapp.dto.ProjectDto;
import academy.mischok.todoapp.model.ProjectEntity;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.repository.ProjectRepository;
import academy.mischok.todoapp.service.ProjectService;
import academy.mischok.todoapp.validation.ProjectValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectEntityConverter projectEntityConverter;

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
    public List<ProjectDto> filterProjects(String title, UserEntity owner) {
        List<ProjectEntity> projects;
        if (title != null && owner != null) {
            projects = projectRepository.findByTitleContainingIgnoreCaseAndOwner(title, owner);
        } else if (title != null) {
            projects = projectRepository.findByTitleContainingIgnoreCase(title);
        } else if (owner != null) {
            projects = projectRepository.findByOwner(owner);
        } else {
            projects = projectRepository.findAll();
        }
        return projects.stream()
                .map(projectEntityConverter::convertToDto)
                .collect(Collectors.toList());
    }
}
