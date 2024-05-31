package academy.mischok.todoapp.controller;

import academy.mischok.todoapp.converter.impl.ToDoEntityConverter;
import academy.mischok.todoapp.dto.ToDoDto;
import academy.mischok.todoapp.model.ToDoEntity;
import academy.mischok.todoapp.repository.ToDoRepository;
import academy.mischok.todoapp.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;
    private final ToDoRepository toDoRepository;
    private final ToDoEntityConverter toDoEntityConverter;


    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ToDoDto> getToDo(@AuthenticationPrincipal ToDoEntity toDo) {
        return ResponseEntity.ok(toDoEntityConverter.convertToDto(toDo));
    }
}
