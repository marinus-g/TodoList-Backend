package academy.mischok.todoapp.service.impl;

import academy.mischok.todoapp.converter.impl.ProjectEntityConverter;
import academy.mischok.todoapp.dto.ProjectDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.repository.ProjectRepository;
import academy.mischok.todoapp.service.ProjectService;
import academy.mischok.todoapp.validation.ProjectValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectEntityConverter projectEntityConverter;

    @Override
    public ProjectValidation isTitleValid(String title) {
        return null;
    }

    @Override
    public Optional<ProjectDto> createProject(ProjectDto projectDto) {
        return Optional.empty();
    }

    @Override
    public List<ProjectDto> findAllProject(UserEntity user) {
        return this.projectRepository.findByOwner(user)
                .stream()
                .map(projectEntityConverter::convertToDto)
                .toList();
    }
}
