package academy.mischok.todoapp.controller;

import academy.mischok.todoapp.dto.ProjectDto;
import academy.mischok.todoapp.model.ProjectEntity;
import academy.mischok.todoapp.service.impl.ProjectServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectControllerTest extends AuthenticatedBaseControllerTest {

    @Test
    void testCreateProject() throws Exception {

        final ObjectMapper objectMapper = new ObjectMapper();
        String contentAsString = mockMvc.perform(get("/project").cookie(super.defaultCookie).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<?> jsonArray = objectMapper.readValue(contentAsString, List.class);
        int length = jsonArray.size();
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


        mockMvc.perform(get("/project").cookie(super.defaultCookie))
                .andExpect(jsonPath("$", hasSize(length+1)));
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