package academy.mischok.todoapp.converter.impl;

import academy.mischok.todoapp.converter.DtoConverter;
import academy.mischok.todoapp.converter.EntityConverter;
import academy.mischok.todoapp.dto.ProjectDto;
import academy.mischok.todoapp.model.ProjectEntity;
import academy.mischok.todoapp.repository.ProjectRepository;
import academy.mischok.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ProjectEntityConverter implements DtoConverter<ProjectDto, ProjectEntity>,
        EntityConverter<ProjectEntity, ProjectDto> {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    public ProjectDto convertToDto(ProjectEntity projectEntity) {
        return ProjectDto.builder()
                .id(projectEntity.getId())
                .title(projectEntity.getTitle())
                .description(projectEntity.getDescription())
                .ownerId(projectEntity.getOwner().getId())
                .build();
    }

    @Override
    public ProjectEntity convertToEntity(ProjectDto projectDto) {
        return ProjectEntity.builder()
                .title(projectDto.getTitle())
                .description(projectDto.getDescription())
                .owner(userRepository.findById(projectDto.getOwnerId()).orElseThrow(()
                        -> new IllegalArgumentException("User with id " + projectDto.getId() + " not found")))
                .build();
    }

    public boolean updateProject (String title, ProjectDto projectDto) {
        Optional<ProjectEntity> projectEntity = userRepository.findById(projectDto.getOwnerId());
        if (optionalProjectEntity.isPresent()) {
            optionalProjectEntity.get();
            projectEntity.setTitel(projectDto.getTitle());
            projectEntity.setDescription(projectDto.getDescription());

            projectRepository.save(projectEntity);
            return true;
        } else {
            return false;
        }
    }
}
