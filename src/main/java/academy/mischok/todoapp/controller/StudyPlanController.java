package academy.mischok.todoapp.controller;

import academy.mischok.todoapp.dto.StudyPlanDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.service.StudyPlanService;
import academy.mischok.todoapp.validation.StudyPlanValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/studyplan")
@RequiredArgsConstructor
public class StudyPlanController {

    private final StudyPlanService studyPlanService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createStudyPlan(@AuthenticationPrincipal UserEntity user,
                                             @Valid @RequestBody StudyPlanDto studyPlanDto) {
        StudyPlanValidation validation = studyPlanService.isStudyPlanValid(studyPlanDto);
        if (!validation.isValid()) {
            return ResponseEntity.badRequest().body(validation.message());
        }
        return studyPlanService.createStudyPlan(user, studyPlanDto)
                .map(dto -> ResponseEntity.created(URI.create("/studyplan/" + dto.getId())).build())
                .orElse(ResponseEntity.noContent().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudyPlan(@AuthenticationPrincipal UserEntity user,
                                             @PathVariable Long id,
                                             @Valid @RequestBody StudyPlanDto studyPlanDto) {
        StudyPlanValidation validation = studyPlanService.isStudyPlanValid(studyPlanDto);
        if (!validation.isValid()) {
            return ResponseEntity.badRequest().body(validation.message());
        }
        return studyPlanService.updateStudyPlan(user, id, studyPlanDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudyPlan(@PathVariable Long id) {
        studyPlanService.deleteStudyPlan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<StudyPlanDto>> getAllStudyPlans(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(studyPlanService.findAllStudyPlans(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudyPlanById(@AuthenticationPrincipal UserEntity user, @PathVariable Long id) {
        return studyPlanService.findByIdAndUser(user, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}