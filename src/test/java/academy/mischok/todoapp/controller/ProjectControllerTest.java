package academy.mischok.todoapp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectControllerTest extends AuthenticatedBaseControllerTest {

    @Test
    void testCreateProject() throws Exception {

        final String data = """
                {
                    "title": "Test Project",
                    "description": "This is a test project"
                }
                """;

        mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("/project/")));
    }

    @Test
    void testCreateProjectWithInvalidName_Blank() throws Exception {

        final String data = """
                {
                    "title": "",
                    "description": "This is a test project"
                }
                """;

        mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Title cannot be blank"));
    }

    @Test
    void testCreateProjectWithInvalidName_Null() throws Exception {

        final String data = """
                {
                    "description": "This is a test project"
                }
                """;

        mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Title cannot be null"));
    }


    @Test
    void testCreateProjectWithNoDescription() throws Exception {

        final String data = """
                {
                    "title": "Test Project"
                }
                """;

        mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("/project/")));
    }

    @Test
    void testCreateProjectWithBlankDescription() throws Exception {

        final String data = """
                {
                    "title": "Test Project",
                    "description": ""
                }
                """;

        mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("/project/")));
    }
}