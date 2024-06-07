package academy.mischok.todoapp.service;

import academy.mischok.todoapp.dto.StudyPlanDto;
import academy.mischok.todoapp.dto.ToDoDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.validation.StudyPlanValidation;

import java.util.List;
import java.util.Optional;

public interface StudyPlanService {
    StudyPlanValidation isStudyPlanValid(StudyPlanDto dto);
    StudyPlanValidation isTitleValid(String title);
    StudyPlanValidation isDateValid(String date);
    boolean existsByTitle(java.lang.String title);
    Optional<StudyPlanDto> createStudyPlan(UserEntity user, StudyPlanDto studyPlanDto);
    Optional<StudyPlanDto> updateStudyPlan(UserEntity user, Long id, StudyPlanDto dto);
    void deleteStudyPlan(Long id);

    List<StudyPlanDto> findAllStudyPlans(UserEntity user);
    Optional<StudyPlanDto> findByIdAndUser(UserEntity user, Long id);
}
