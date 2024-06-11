package academy.mischok.todoapp.controller;

import academy.mischok.todoapp.converter.impl.ProjectEntityConverter;
import academy.mischok.todoapp.dto.ProjectDto;
import academy.mischok.todoapp.model.ProjectEntity;
import academy.mischok.todoapp.repository.ProjectRepository;
import academy.mischok.todoapp.service.impl.ProjectServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectControllerTest extends AuthenticatedBaseControllerTest {

    @Mock
    private ProjectEntityConverter projectEntityConverter;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectController projectController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
                .andExpect(jsonPath("$", hasSize(length + 1)));
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

    @Test
    void testUpdateProject() throws Exception {

        final String data = """
                {
                    "title": "Test Project",
                    "description": "This is a test project"
                }
                """;

        final String location = mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("/project/")))
                .andReturn()
                .getResponse().getHeader("Location");

        assert location != null;
        final String generatedProject = mockMvc.perform(get(location)
                .cookie(super.defaultCookie))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        final ObjectMapper objectMapper = new ObjectMapper();
        ProjectDto projectDto = objectMapper.readValue(generatedProject, ProjectDto.class);
        projectDto.setTitle("Updated Title");
        projectDto.setDescription("Updated Description");

        mockMvc.perform(put(location)
                .cookie(super.defaultCookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectDto)))
                        .andExpect(status().isOk());
    }

    @Test
    void testDeleteProject() throws Exception {
        final String data = """
            {
                "title": "Test Project",
                "description": "This is a test project"
            }
            """;

        final String location = mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("/project/")))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assert location != null;

        mockMvc.perform(get(location)
                        .cookie(super.defaultCookie))
                .andExpect(status().isOk());

        mockMvc.perform(delete(location)
                        .cookie(super.defaultCookie))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(location)
                        .cookie(super.defaultCookie))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProject_NotFound() throws Exception {


        mockMvc.perform(delete("/project/" +- 1)
                        .cookie(super.defaultCookie))
                .andExpect(status().isNotFoundp());

    }
}
