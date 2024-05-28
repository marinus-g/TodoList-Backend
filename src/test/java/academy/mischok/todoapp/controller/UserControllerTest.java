package academy.mischok.todoapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateValidUser() throws Exception {
        final String data = """
                {
                    "username": "test",
                    "email": "test@gmail.de"
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
                    "email": "jdjdjd.de"
                    "password": "Abc1234!"
                }
                """;
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid email address"));
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
    void testCreatedUserWithInvalidUsername() throws Exception {
        final String data = """
                {
                    "username": "t",
                    "email": "",
                    "password": "Abc1234!"
                }
                """;

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username must be at least 3 characters long"));
    }
}