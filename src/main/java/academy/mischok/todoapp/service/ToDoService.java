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
    Optional<ToDoEntity> findToDoByTitle(java.lang.String title);

    Optional<ToDoEntity> findToDoByTitleAndUser(String title, UserEntity user);

    boolean existsByTitle(java.lang.String title);
    void deleteToDo(Long id);
    Optional<ToDoDto> createToDo(UserEntity user, ToDoDto dto);
    TodoValidation isValidTodo(ToDoDto dto);
    TodoValidation isTitleValid(java.lang.String title);
    TodoValidation isDescriptionValid(java.lang.String description);
    TodoValidation isDateValid(String date);
    TodoValidation isStatusValid(String status);
    List<ToDoDto> findAllToDosByUser(UserEntity user);
    Optional<ToDoDto> findByIdAndUser(UserEntity user, Long id);
}
