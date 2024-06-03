package academy.mischok.todoapp.service.impl;

import academy.mischok.todoapp.converter.impl.ToDoEntityConverter;
import academy.mischok.todoapp.dto.ToDoDto;
import academy.mischok.todoapp.model.ToDoEntity;
import academy.mischok.todoapp.repository.ToDoRepository;
import academy.mischok.todoapp.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {
    private final ToDoRepository toDoRepository;
    private final ToDoEntityConverter toDoEntityConverter;

    @Override
    public List<ToDoDto> findAllToDos() {
        List<ToDoEntity> users = toDoRepository.findAll();
        return users.stream().map(toDoEntityConverter::convertToDto)
                .toList();
    }

    @Override
    public Optional<ToDoEntity> findToDoByTitle(String title) {
        return this.toDoRepository.findByTitle(title);
    }

    @Override
    public boolean existsByTitle(String title) {
        return this.toDoRepository.existsByTitle(title);
    }

    @Override
    public void deleteToDo(Long id) {
        this.toDoRepository.deleteById(id);
    }
}
