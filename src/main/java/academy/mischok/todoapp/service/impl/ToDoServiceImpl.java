package academy.mischok.todoapp.service.impl;

import academy.mischok.todoapp.converter.impl.DateConverter;
import academy.mischok.todoapp.converter.impl.ToDoEntityConverter;
import academy.mischok.todoapp.dto.ToDoDto;
import academy.mischok.todoapp.model.ToDoEntity;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.repository.ToDoRepository;
import academy.mischok.todoapp.service.ToDoService;
import academy.mischok.todoapp.validation.TodoValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {
    private final ToDoRepository toDoRepository;
    private final ToDoEntityConverter toDoEntityConverter;
    private final DateConverter dateConverter;

    @Override
    public List<ToDoDto> findAllToDos() {
        List<ToDoEntity> users = toDoRepository.findAll();
        return users.stream()
                .map(toDoEntityConverter::convertToDto)
                .toList();
    }

    @Override
    public Optional<ToDoEntity> findToDoByTitle(java.lang.String title) {
        return this.toDoRepository.findByTitle(title);
    }

    @Override
    public boolean existsByTitle(java.lang.String title) {
        return this.toDoRepository.existsByTitle(title);
    }



    @Override
    public Optional<ToDoDto> createToDo(UserEntity user, ToDoDto dto) {
        return Optional.ofNullable(dto)
                .map(toDoEntityConverter::convertToEntity)
                .stream()
                .peek(toDoEntity -> toDoEntity.setUser(user))
                .map(toDoRepository::save)
                .map(toDoEntityConverter::convertToDto)
                .findFirst();
    }

    @Override
    public void deleteToDo(Long id){
        if (toDoRepository.existsById(id)){
            toDoRepository.deleteById(id);
        }
    }

    public Optional<ToDoDto> findByIdAndUser(UserEntity user, Long id) {
        return toDoRepository.findByIdAndUser(id, user)
                .map(toDoEntityConverter::convertToDto);
    }

    @Override
    public TodoValidation isValidTodo(ToDoDto dto) {
        TodoValidation validation;
        if (!(validation = isTitleValid(dto.getTitle())).isValid()) {
            return validation;
        } else if (!(validation = isDescriptionValid(dto.getDescription())).isValid()) {
            return validation;
        } else if(!(validation = isDateValid(dto.getStartDate())).isValid()) {
            return validation;
        } else if (!(validation = isDateValid(dto.getEndDate())).isValid()) {
            return validation;
        } else if (!(validation = isStatusValid(dto.getStatus())).isValid()) {
            return validation;
        }
        return new TodoValidation(true, null);
    }

    @Override
    public TodoValidation isTitleValid(java.lang.String title) {
        if (title == null) {
            return new TodoValidation(false, "Title cannot be null");
        } else if (title.isBlank()) {
            return new TodoValidation(false, "Title cannot be blank");
        }
        return new TodoValidation(true, null);
    }

    @Override
    public TodoValidation isDescriptionValid(java.lang.String description) {
        if (description == null || description.isBlank()) {
            return new TodoValidation(false, "Description should not be empty");
        }
        return new TodoValidation(true, null);
    }
    @Override
    public TodoValidation isDateValid(String date) {
        if (Objects.isNull(date)) {
            return new TodoValidation(true, null);
        }
        final Date dateObject = this.dateConverter.convertToEntity(date);
        return new TodoValidation(dateObject != null, dateObject == null ? "Invalid Date" : null);
    }
    @Override
    public TodoValidation isStatusValid(String status){
        if (status == null) {
            return new TodoValidation(false, "Status cannot be null");
        } else if (status.isBlank()) {
            return new TodoValidation(false, "Status cannot be blank");
        } else if (!status.equalsIgnoreCase("todo")
                && !status.equalsIgnoreCase("doing")
                && !status.equalsIgnoreCase("done")){
            return new TodoValidation(false,"Invalid Status");
        }
        return new TodoValidation(true,null);

    }

    @Override
    public List<ToDoDto> findAllToDosByUser(UserEntity user) {
        return this.toDoRepository.findByUser(user)
                .stream()
                .map(toDoEntityConverter::convertToDto)
                .toList();
    }
}
