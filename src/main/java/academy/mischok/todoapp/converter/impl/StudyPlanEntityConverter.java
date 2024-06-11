package academy.mischok.todoapp.converter.impl;


import academy.mischok.todoapp.converter.DtoConverter;
import academy.mischok.todoapp.converter.EntityConverter;
import academy.mischok.todoapp.dto.StudyPlanDto;
import academy.mischok.todoapp.model.StudyPlanEntity;
import academy.mischok.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
@RequiredArgsConstructor
public class StudyPlanEntityConverter implements DtoConverter<StudyPlanDto, StudyPlanEntity>,
        EntityConverter<StudyPlanEntity, StudyPlanDto> {
    private final DateConverter dateConverter;
    private final UserRepository userRepository;

    @Override
    public StudyPlanDto convertToDto(StudyPlanEntity studyPlanEntity) {
        return StudyPlanDto.builder()
                .id(studyPlanEntity.getId())
                .title(studyPlanEntity.getTitle())
                .startDate(studyPlanEntity.getStartDate() == null ? null : studyPlanEntity.getStartDate().toString())
                .endDate(studyPlanEntity.getEndDate() == null ? null : studyPlanEntity.getEndDate().toString())
                .ownerId(studyPlanEntity.getOwner().getId())
                .build();
    }

    @Override
    public StudyPlanEntity convertToEntity(StudyPlanDto studyPlanDto) {
        return StudyPlanEntity.builder()
                .id(studyPlanDto.getId())
                .title(studyPlanDto.getTitle())
                .startDate(studyPlanDto.getStartDate() == null ? null : new Date(dateConverter.convertToEntity(studyPlanDto.getStartDate()).getTime()))
                .endDate(studyPlanDto.getEndDate() == null ? null : new Date(dateConverter.convertToEntity(studyPlanDto.getEndDate()).getTime()))
                .owner(userRepository.findById(studyPlanDto.getOwnerId()).orElseThrow(()
                        -> new IllegalArgumentException("User with id " + studyPlanDto.getId() + " not found")))
                .build();
    }
}

