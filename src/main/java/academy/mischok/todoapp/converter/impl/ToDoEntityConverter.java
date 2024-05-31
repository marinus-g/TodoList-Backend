package academy.mischok.todoapp.converter.impl;

import academy.mischok.todoapp.converter.DtoConverter;
import academy.mischok.todoapp.converter.EntityConverter;
import academy.mischok.todoapp.dto.ToDoDto;
import academy.mischok.todoapp.dto.UserDto;
import academy.mischok.todoapp.model.ToDoEntity;
import academy.mischok.todoapp.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ToDoEntityConverter implements DtoConverter<ToDoDto, ToDoEntity>,
        EntityConverter<ToDoEntity, ToDoDto> {
    @Override
    public ToDoDto convertToDto(ToDoEntity toDoEntity) {
        return ToDoDto.builder()
                .id(toDoEntity.getId())
                .title(toDoEntity.getTitle())
                .description(toDoEntity.getDescription())
                .startDate(toDoEntity.getStartDate())
                .endDate(toDoEntity.getEndDate())
                .build();

    }

    @Override
    public ToDoEntity convertToEntity(ToDoDto toDoDto) {
        return ToDoEntity.builder()
                .id(toDoDto.getId())
                .title(toDoDto.getTitle())
                .description(toDoDto.getDescription())
                .startDate(toDoDto.getStartDate())
                .endDate(toDoDto.getEndDate())
                .build();
    }
}
