package academy.mischok.todoapp.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends BaseControllerTest {

    @Test
    void testCreateValidUser() throws Exception {
        final String data = """
                {
                    "username": "test",
                    "email": "test@gmail.de",
                    "password": "Abc1234!"
                }
                """;
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void testCreatedUserWithInvalidEmail() throws Exception {
        final String data = """
                {
                    "username": "test",
                    "email": "jdjdjd.de",
                    "password": "Abc1234!"
                }
                """;
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Invalid email address"));
    }

    @Test
    void testCreateUserWithInvalidCharacterInUsername() throws Exception {
        final String data = """
                {
                    "username": "täest",
                    "email": "test@gmail.com",
                    "password": "Abc1234!"
                }
                """;
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Username should only contains numeric and alphabetic characters"));
    }

    @Test
    void testCreatedUserWithInvalidPassword() throws Exception {
        final String data = """
                {
                    "username": "test",
                    "email": "test@gmail.com",
                    "password": "abc"
                }
                """;
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreatedUserWithUsername_TooShort_BadRequest() throws Exception {
        final String data = """
                {
                    "username": "t",
                    "email": "test@test.de",
                    "password": "Abc1234!"
                }
                """;

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Username too short"));
    }

    @Test
    void testCreateUserWithBlankUserName() throws Exception {
        final String data = """
                {
                    "username": "",
                    "email": "test@gmail.de",
                    "password": "Abc1234!"
                }
                """;
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Username too short"));
    }

    @Test
    void testAuthenticateValidUser() throws Exception {
        final String data = """
                {
                    "login": "default-user",
                    "password": "Abc1234!"
                }
                """;
        final String cookie = Arrays.stream(mockMvc.perform(post("/user/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data)
                        )
                        .andExpect(status().isOk())
                        .andExpect(cookie().exists("token"))
                        .andReturn()
                        .getResponse()
                        .getCookies())
                .filter(c -> "token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst().orElseThrow();

        mockMvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("token", cookie))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("default-user"))
                .andExpect(jsonPath("$.email").value("base-test@gmail.de"));

    }
}