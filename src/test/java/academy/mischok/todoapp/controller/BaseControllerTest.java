package academy.mischok.todoapp.controller;

import academy.mischok.todoapp.dto.RegistrationDto;
import academy.mischok.todoapp.dto.UserDto;
import academy.mischok.todoapp.model.UserEntity;
import academy.mischok.todoapp.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class BaseControllerTest {

    @Autowired
    private UserService userService;
    protected UserDto createdUserDto;
    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.createdUserDto = this.userService.createUser(RegistrationDto
                .builder()
                .username("default-user")
                .email("base-test@gmail.de")
                .password("Abc1234!")
                .build()
        ).orElseThrow();
    }

    @AfterEach
    void tearDown() {
        this.userService.findUserByName("default-user")
                .ifPresent(userEntity -> this.userService.deleteUser(userEntity.getId()));
    }
}