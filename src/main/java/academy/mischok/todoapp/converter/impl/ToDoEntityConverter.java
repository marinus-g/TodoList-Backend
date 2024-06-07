package academy.mischok.todoapp.converter.impl;

import academy.mischok.todoapp.converter.DtoConverter;
import academy.mischok.todoapp.converter.EntityConverter;
import academy.mischok.todoapp.dto.ToDoDto;
import academy.mischok.todoapp.model.ToDoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;


@Component
@RequiredArgsConstructor
public class ToDoEntityConverter implements DtoConverter<ToDoDto, ToDoEntity>,
        EntityConverter<ToDoEntity, ToDoDto> {

    private final DateConverter dateConverter;

    @Override
    public ToDoDto convertToDto(ToDoEntity toDoEntity) {
        return ToDoDto.builder()
                .id(toDoEntity.getId())
                .title(toDoEntity.getTitle())
                .description(toDoEntity.getDescription())
                .startDate(toDoEntity.getStartDate() == null ? null : toDoEntity.getStartDate().toString())
                .endDate(toDoEntity.getEndDate() == null ? null : toDoEntity.getEndDate().toString())
                .build();

    }

    @Override
    public ToDoEntity convertToEntity(ToDoDto toDoDto) {
        return ToDoEntity.builder()
                .id(toDoDto.getId())
                .title(toDoDto.getTitle())
                .description(toDoDto.getDescription())
                .startDate(toDoDto.getStartDate() == null ? null : new Date(dateConverter.convertToEntity(toDoDto.getStartDate()).getTime()))
                .endDate(toDoDto.getEndDate() == null ? null : new Date(dateConverter.convertToEntity(toDoDto.getEndDate()).getTime()))
                .build();
    }
}
