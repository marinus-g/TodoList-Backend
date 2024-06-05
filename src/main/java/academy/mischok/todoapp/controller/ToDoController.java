package academy.mischok.todoapp.controller;

import academy.mischok.todoapp.converter.impl.ToDoEntityConverter;
import academy.mischok.todoapp.dto.ToDoDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.service.ToDoService;
import academy.mischok.todoapp.service.UserService;
import academy.mischok.todoapp.validation.TodoValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class ToDoController {

    private final ToDoService toDoService;
    private final UserService userService;
    private final ToDoEntityConverter toDoEntityConverter;

    /*

    User ist eingeloggt und macht get auf /todo
    --> /todo weiß das er eingeloggt ist und weiß daher welchen User er benutzen muss
    -->

     */

    // POST -> /todo/ -> token cookie wird mitgeschickt -> /todo weiß daher welcher user den post macht

    // GET test.de/todo/1 <-- gibt title, id, beschreibung des todos zurück
        // findByIdAndUser(todoId, userId)
    // GET test.de/todo <--- gibt liste aller todo title + ids zurück

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "/{id}"
    )


    public ResponseEntity<ToDoDto> getTodoById(@PathVariable UserEntity user, Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        Long userId = userEntity.getId();

        UserEntity user1 = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Optional<ToDoDto> todoDto = toDoService.findAllToDosByUser(user);

        return toDoService.findToDo(user, id);
        // return todoService.findTodo(user, id);
        //return ResponseEntity.ok(toDoEntityConverter.convertToDto(toDo));
    }
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
        public  ResponseEntity<?> deleteTodo(@AuthenticationPrincipal UserEntity user,
                                                   @Valid @RequestBody ToDoDto toDoDto) {
            final TodoValidation todoValidation = toDoService.isValidTodo(toDoDto);
            if (!todoValidation.isValid()) {
                return ResponseEntity.badRequest().body(todoValidation.message());
            }
        return  ResponseEntity.ok(); //toDoService.deleteToDo(toDoDto.getId());
        }
    }
