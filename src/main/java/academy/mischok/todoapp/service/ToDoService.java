package academy.mischok.todoapp.service;

import academy.mischok.todoapp.dto.ToDoDto;
import academy.mischok.todoapp.model.ToDoEntity;
import academy.mischok.todoapp.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface ToDoService {
    List<ToDoDto> findAllToDos();
    Optional<ToDoEntity> findToDoByTitle(String title);
    boolean existsByTitle(String title);
    void deleteToDo(Long id);
}
