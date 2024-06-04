package academy.mischok.todoapp.controller;

import academy.mischok.todoapp.converter.impl.UserEntityConverter;
import academy.mischok.todoapp.dto.LoginPasswordDto;
import academy.mischok.todoapp.dto.RegistrationDto;
import academy.mischok.todoapp.dto.UserDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.repository.UserRepository;
import academy.mischok.todoapp.service.AuthenticationService;
import academy.mischok.todoapp.service.UserService;
import academy.mischok.todoapp.service.impl.UserServiceImpl;
import academy.mischok.todoapp.validation.UserNameValidation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final UserEntityConverter userEntityConverter;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createUser(@Valid @RequestBody
                                                  final RegistrationDto dto) {

        System.out.println("IN CREATE USER POST");
        UserNameValidation validation = userService.isValidUsername(dto.getUsername());
        if (!validation.isValid()) {
            return ResponseEntity.badRequest().body(validation.message());
        }

        if (userService.existsByUsername(dto.getUsername())) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        if (userService.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        return
                this.userService.createUser(dto)
                        .map(userDto -> ResponseEntity.created(URI.create("/user/" + userDto.getId()))
                                .build())
                        .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping(
            value = "/authenticate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> authenticateUser(HttpServletResponse response,
                                              @AuthenticationPrincipal UserEntity user) {
         return Optional.ofNullable(user)
                .map(userEntity -> {
                    final Cookie cookie = authenticationService.buildCookie(userEntity)
                            .orElseThrow(() -> new RuntimeException("Cookie not created"));
                    response.addCookie(cookie);
                    return ResponseEntity.ok(userEntityConverter.convertToDto(userEntity));
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal UserEntity user) {
        return ResponseEntity.ok(userEntityConverter.convertToDto(user));
    }
}