package academy.mischok.todoapp.controller;

import academy.mischok.todoapp.dto.ToDoDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.service.ToDoService;
import academy.mischok.todoapp.service.UserService;
import academy.mischok.todoapp.validation.TodoValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;
    private final UserService userService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ToDoDto> getToDoById(@AuthenticationPrincipal UserEntity userEntity, @PathVariable Long id) {

        return toDoService.findByIdAndUser(userEntity, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal UserEntity user,
                                              @Valid @RequestBody ToDoDto toDoDto) {
        final TodoValidation todoValidation = toDoService.isValidTodo(toDoDto);
        if (!todoValidation.isValid()) {
            return ResponseEntity.badRequest().body(todoValidation.message());
        }
        return toDoService.createToDo(user, toDoDto)
                .map(saved -> ResponseEntity.created(URI.create("/todo/" + saved.getId())).build())
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping(value = "/update",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal UserEntity user,
                                        @Valid @RequestBody ToDoDto toDoDto, Long id) {
        final TodoValidation todoValidation = toDoService.isValidTodo(toDoDto);
        if (!todoValidation.isValid()) {
            return ResponseEntity.badRequest().body(todoValidation.message());
        }
        return toDoService.updateToDo(user, id, toDoDto)
                .map(saved -> ResponseEntity.created(URI.create("/todo/" + saved.getId())).build())
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<?> deleteTodo(@AuthenticationPrincipal UserEntity user,
                                         @Valid @RequestBody ToDoDto toDoDto) {
        final TodoValidation todoValidation = toDoService.isValidTodo(toDoDto);
        if (!todoValidation.isValid()) {
            return ResponseEntity.badRequest().body(todoValidation.message());
        }
        if (toDoService.existsByTitle(toDoDto.getTitle())){
            toDoService.deleteToDo(toDoDto.getId());
            return  ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
