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
public class ToDoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateValidToDo() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen."
                    "start_date": "12-12-2020",
                    "end_date": "14-12-2020",
                    "status": "ToDo"
                }
                """;
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void testCreatedToDoWithInvalidStartDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen."
                    "start_date": "12-2020",
                    "end_date": "14-12-2020",
                    "status": "ToDo"
                }
                """;

        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid Start Date"));
    }

    @Test
    void testCreatedToDoWithInvalidEndDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen."
                    "start_date": "13-12-2020",
                    "end_date": "12-2020",
                    "status": "ToDo"
                }
                """;

        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid End Date"));
    }
}
