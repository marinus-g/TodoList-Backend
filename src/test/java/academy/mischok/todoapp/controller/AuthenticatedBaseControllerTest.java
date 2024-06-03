package academy.mischok.todoapp.controller;

import academy.mischok.todoapp.dto.RegistrationDto;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public abstract class AuthenticatedBaseControllerTest extends BaseControllerTest {

    protected Cookie defaultCookie;

    @BeforeEach
    void authenticate() throws Exception {
        final String data = """
                {
                    "login": "default-user",
                    "password": "Abc1234!"
                }
                """;
        this.defaultCookie = Arrays.stream(mockMvc.perform(post("/user/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data)
                        )
                        .andExpect(status().isOk())
                        .andExpect(cookie().exists("token"))
                        .andReturn()
                        .getResponse()
                        .getCookies())
                .filter(c -> "token".equals(c.getName()))
                .findFirst().orElseThrow();
    }
}
