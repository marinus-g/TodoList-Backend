package academy.mischok.todoapp.service;

import academy.mischok.todoapp.dto.ToDoDto;
import academy.mischok.todoapp.model.Status;
import academy.mischok.todoapp.model.ToDoEntity;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.validation.TodoValidation;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ToDoService {
    List<ToDoDto> findAllToDos();
    Optional<ToDoEntity> findToDoByTitle(String title);
    boolean existsByTitle(String title);
    void deleteToDo(Long id);
    Optional<ToDoDto> createToDo(UserEntity user, ToDoDto dto);
    TodoValidation isValidTodo(ToDoDto dto);
    TodoValidation isTitleValid(String title);
    TodoValidation isDescriptionValid(String description);
    TodoValidation isDateValid(Date date);
    TodoValidation isStatusValid(Status status);
    List<ToDoDto> findAllToDosByUser(UserEntity user);
    Optional<ToDoDto> findByIdAndUser(UserEntity user, Long id);
}
