package academy.mischok.todoapp.controller;


import academy.mischok.todoapp.converter.impl.ProjectEntityConverter;
import academy.mischok.todoapp.dto.ProjectDto;
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

    @GetMapping("/filter")
    public ResponseEntity<List<ProjectDto>> filterProjects(@RequestParam(required = false) String title,
                                                           @RequestParam(required = false) Long ownerId) {
        UserEntity owner = null;
        if (ownerId != null) {
            owner = new UserEntity();
            owner.setId(ownerId);  // Assuming you have a UserEntity with just an ID set
        }
        return ResponseEntity.ok(projectService.filterProjects(title, owner));
    }

}
