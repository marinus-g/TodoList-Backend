package academy.mischok.todoapp.service.impl;

import academy.mischok.todoapp.converter.impl.DateConverter;
import academy.mischok.todoapp.converter.impl.StudyPlanEntityConverter;
import academy.mischok.todoapp.dto.StudyPlanDto;
import academy.mischok.todoapp.model.StudyPlanEntity;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.repository.StudyPlanRepository;
import academy.mischok.todoapp.service.StudyPlanService;
import academy.mischok.todoapp.validation.StudyPlanValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyPlanServiceImpl implements StudyPlanService {
    private final StudyPlanRepository studyPlanRepository;
    private final StudyPlanEntityConverter studyPlanEntityConverter;
    private final DateConverter dateConverter;

    @Override
    public StudyPlanValidation isStudyPlanValid(StudyPlanDto dto) {
        StudyPlanValidation validation;
        if (!(validation = isTitleValid(dto.getTitle())).isValid()) {
            return validation;
        } else if (!(validation = isDateValid(dto.getStartDate())).isValid()) {
            return validation;
        } else if (!(validation = isDateValid(dto.getEndDate())).isValid()) {
            return validation;
        }
        return new StudyPlanValidation(true, null);
    }

    @Override
    public StudyPlanValidation isTitleValid(String title) {
        if (title == null || title.isBlank()) {
            return new StudyPlanValidation(false, "Title should not be empty");
        } else if (title.length() < 3) {
            return new StudyPlanValidation(false, "Title too short");
        }
        return new StudyPlanValidation(true, null);
    }

    @Override
    public StudyPlanValidation isDateValid(String date) {
        if (Objects.isNull(date)) {
            return new StudyPlanValidation(true, null);
        }
        final Date dateObject = this.dateConverter.convertToEntity(date);
        return new StudyPlanValidation(dateObject != null, dateObject == null ? "Invalid Date" : null);

    }

    @Override
    public boolean existsByTitle(String title) {
        return this.studyPlanRepository.existsByTitle(title);
    }

    @Override
    public Optional<StudyPlanDto> createStudyPlan(UserEntity user,StudyPlanDto studyPlanDto) {
        return Optional.ofNullable(studyPlanDto)
                .map(studyPlanEntityConverter::convertToEntity)
                .stream()
                .peek(studyPlanEntity -> studyPlanEntity.setOwner(user))
                .map(studyPlanRepository::save)
                .map(studyPlanEntityConverter::convertToDto)
                .findFirst();
    }

    @Override
    public Optional<StudyPlanDto> updateStudyPlan(UserEntity user, Long id, StudyPlanDto dto) {
        return studyPlanRepository.findByIdAndUser(id, user)
                .map(selectedToDo -> {
                    selectedToDo.setTitle(dto.getTitle());
                    selectedToDo.setStartDate(java.sql.Date.valueOf(dto.getStartDate()));
                    selectedToDo.setEndDate(java.sql.Date.valueOf(dto.getEndDate()));
                    return studyPlanRepository.save(selectedToDo);
                })
                .map(studyPlanEntityConverter::convertToDto);

    }

    @Override
    public void deleteStudyPlan(Long id) {
        if (studyPlanRepository.existsById(id)) {
            studyPlanRepository.deleteById(id);
        }
    }

    @Override
    public List<StudyPlanDto> findAllStudyPlans(UserEntity user) {
        List<StudyPlanEntity> users = studyPlanRepository.findAll();
        return users.stream()
                .map(studyPlanEntityConverter::convertToDto)
                .toList();
    }

    @Override
    public Optional<StudyPlanDto> findByIdAndUser(UserEntity user, Long id) {
        return studyPlanRepository.findByIdAndUser(id, user)
                .map(studyPlanEntityConverter::convertToDto);

    }
}
