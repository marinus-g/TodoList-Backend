package academy.mischok.todoapp.controller;


import academy.mischok.todoapp.converter.impl.ProjectEntityConverter;
import academy.mischok.todoapp.dto.ProjectDto;
import academy.mischok.todoapp.model.ProjectEntity;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.repository.ProjectRepository;
import academy.mischok.todoapp.service.ProjectService;
import academy.mischok.todoapp.validation.ProjectValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectRepository projectRepository;
    private final ProjectEntityConverter projectEntityConverter;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createProject(@AuthenticationPrincipal UserEntity userEntity,
                                           @Valid @RequestBody ProjectDto projectDto) {
        ProjectValidation validation = projectService.isTitleValid(projectDto.getTitle());
        if (!validation.isValid()) {
            return ResponseEntity.badRequest().body(validation.message());
        }
        projectDto.setOwnerId(userEntity.getId());
        return this.projectService.createProject(projectDto)
                .map(dto -> ResponseEntity.created(URI.create("/project/" + dto.getId())).build())
                .orElse(ResponseEntity.noContent().build());

    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getAllProjects(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(projectService.findAllProject(user));
    }

    @PutMapping("/{titel}")
    public ResponseEntity<String> updateProject(@PathVariable String title, @RequestBody ProjectDto projectDto) {
        boolean isUpdated = projectEntityConverter.updateProject(title, projectDto);
        if (isUpdated) {
            return ResponseEntity.ok("Project updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Project update failed.");
        }
    }

    @DeleteMapping("/{title}")
            public ResponseEntity<String> deleteProject(@PathVariable String title, @PathVariable Long id) {
        ProjectEntity project = projectRepository.findById(id).orElse(null);

        if (project != null) {
            projectRepository.delete(project);

            return ResponseEntity.ok("Project deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("Project not found.");
        }
    }
}
