package academy.mischok.todoapp.controller;


import academy.mischok.todoapp.dto.ProjectDto;
import academy.mischok.todoapp.model.ProjectEntity;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.repository.ProjectRepository;
import academy.mischok.todoapp.service.ProjectService;
import academy.mischok.todoapp.service.impl.ProjectNotFoundException;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getProject(@AuthenticationPrincipal UserEntity user, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(projectService.findProjectByIdAndUser(id, user));
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        return this.projectService.updateProject(id, projectDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/{projectId}/users/{userId}")
    public ResponseEntity<?> addUserToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        try {
            projectService.addUserToProject(projectId, userId);
            return ResponseEntity.ok().build();
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{projectId}/users/{userId}")
    public ResponseEntity<?> removeUserFromProject(@PathVariable Long projectId, @PathVariable Long userId) {
        try {
            projectService.removeUserFromProject(projectId, userId);
            return ResponseEntity.noContent().build();
        } catch (ProjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
            public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        ProjectEntity project = projectRepository.findById(id).orElse(null);

        if (project != null) {
            projectRepository.delete(project);

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
