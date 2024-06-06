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
                    "description": "Bessere Tests machen.",
                    "start_date": "13-12-2020",
                    "end_date": "14-12-2020",
                    "status": "TODO"
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
                    "description": "Bessere Tests machen.",
                    "start_date": "12-2020",
                    "end_date": "14-12-2020",
                    "status": "TODO"
                }
                """;

        mockMvc.perform(post("/todo")
                        .cookie(super.defaultCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid Date"));
    }

    @Test
    void testCreatedToDoWithInvalidEndDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen.",
                    "start_date": "13-12-2020",
                    "end_date": "12-2020",
                    "status": "TODO"
                }
                """;

        mockMvc.perform(post("/todo")
                        .cookie(super.defaultCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid Date"));
    }

    @Test
    void testCreateTodoWithNoStartDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen.",
                    "end_date": "14-12-2020",
                    "status": "TODO"
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
    void testCreateTodoWithNoEndDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen.",
                    "start_date": "14-12-2020",
                    "status": "TODO"
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
    void testCreateTodoWithNoDates() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen.",
                    "status": "TODO"
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
    void testCreatedToDoWithInvalidStatus() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "Bessere Tests machen.",
                    "start_date": "13-12-2020",
                    "end_date": "14-12-2020",
                    "status": "asdf"
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
                    "status": "TODO"
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
                    "status": "TODO"
                }
                """;
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title should not be empty"));
    }

    @Test
    void testCreatedToDoWithInvalidTitle_Null() throws Exception {
        final String data = """
                {
                    "description": "Bessere Tests machen.",
                    "status": "TODO"
                }
                """;
        mockMvc.perform(post("/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title should not be empty"));
    }

    @Test
    void testCreatedToDoWithInvalidDescription_Null() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "status": "TODO"
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
                    "status": "TODO"
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
                    "status": "TODO"
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
    @Test
    void testUpdatedToDo_Success() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "description": "B",
                    "status": "TODO"
                }
                """;
        mockMvc.perform(post("/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("ToDo not succesfully updated"));
    }
}