package academy.mischok.todoapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TodoControllerTest extends AuthenticatedBaseControllerTest {

    @Test
    void testCreateValidToDo() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen."
                    "start_date": "12-12-2020",
                    "end_date": "14-12-2020",
                    "state": "todo"
                }
                """;
        mockMvc.perform(post("/todo")
                        .cookie(super.defaultCookie)
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
                    "state": "todo"
                }
                """;

        mockMvc.perform(post("/todo")
                        .cookie(super.defaultCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid Start Date"));
    }

    @Test
    void testCreatedToDoWithInvalidEndDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen."
                    "start_date": "13-12-2020",
                    "end_date": "12-2020",
                    "state": "todo"
                }
                """;

        mockMvc.perform(post("/todo")
                        .cookie(super.defaultCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid End Date"));
    }

    @Test
    void testCreateTodoWithNoStartDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen."
                    "end_date": "14-12-2020",
                    "state": "todo"
                }
                """;

        mockMvc.perform(post("/todo")
                        .cookie(super.defaultCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isOk());
    }

    @Test
    void testCreateTodoWithNoEndDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen."
                    "start_date": "13-12-2020",
                    "state": "todo"
                }
                """;

        mockMvc.perform(post("/todo")
                        .cookie(super.defaultCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isOk());
    }

    @Test
    void testCreateTodoWithNoDates() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen."
                    "state": "todo"
                }
                """;

        mockMvc.perform(post("/todo")
                        .cookie(super.defaultCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isOk());
    }

    @Test
    void testCreatedToDoWithInvalidStatus() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen."
                    "start_date": "13-12-2020",
                    "end_date": "14-12-2020",
                    "state": "tod"
                }
                """;

        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(super.defaultCookie)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid Status"));
    }


    @Test
    void testCreatedToDoWithInvalidDescription_Empty() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "",
                    "state": "todo"
                }
                """;
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Description should not be empty"));
    }

    @Test
    void testCreatedToDoWithInvalidTitle_Empty() throws Exception {
        final String data = """
                {
                    "title": "",
                    "description": "Bessere Tests machen.",
                    "state": "todo"
                }
                """;
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Title should not be empty"));
    }

    @Test
    void testCreatedToDoWithInvalidTitle_Null() throws Exception {
        final String data = """
                {
                    "description": "Bessere Tests machen.",
                    "state": "todo"
                }
                """;
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Title should not be empty"));
    }

    @Test
    void testCreatedToDoWithInvalidDescription_Null() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "state": "todo"
                }
                """;
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Description should not be empty"));
    }

    @Test
    void testCreatedToDoWithInvalidTitle_TooShort() throws Exception {
        final String data = """
                {
                    "title": "t",
                    "description": "Bessere Tests machen.",
                    "state": "todo"
                }
                """;
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Title too short"));
    }

    @Test
    void testCreatedToDoWithInvalidDescription_TooShort() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "B",
                    "state": "todo"
                }
                """;
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Description too short"));
    }
}