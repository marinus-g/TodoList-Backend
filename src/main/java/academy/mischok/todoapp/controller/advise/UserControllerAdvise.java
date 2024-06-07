package academy.mischok.todoapp.controller.advise;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class UserControllerAdvise {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
            MethodArgumentNotValidException e) {
        final Map<String, String> map = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(
                fieldError -> map.put(fieldError.getField(),
                        fieldError.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(map);
    }

}
