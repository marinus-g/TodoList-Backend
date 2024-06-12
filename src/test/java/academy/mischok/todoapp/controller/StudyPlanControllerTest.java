package academy.mischok.todoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StudyPlanControllerTest extends AuthenticatedBaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "testUser")
    void testCreateValidStudyPlan() throws Exception {
        String contentAsString = mockMvc.perform(get("/studyplan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(super.defaultCookie))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<?> jsonArray = objectMapper.readValue(contentAsString, List.class);
        int length = jsonArray.size();
        final String data = """
                {
                    "title": "Test Studyplan",
                    "start_date": "2024-04-20",
                    "end_date": "2024-04-24"
                }
                """;
        mockMvc.perform(post("/studyplan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("/studyplan/")));
        mockMvc.perform(get("/studyplan").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(length + 1)));
    }

    @Test
    void createdStudyPlanWithShortTitle() throws Exception {
        final String data = """
                {
                    "title": "T",
                    "start_date": "2024-04-20",
                    "end_date": "2024-04-24"
                }
                """;

        mockMvc.perform(post("/studyplan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(super.defaultCookie))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Title too short"));
    }

    @Test
    void createdStudyPlanWithBlankTitle() throws Exception {
        final String data = """
                {
                    "title": "",
                    "start_date": "2024-04-20",
                    "end_date": "2024-04-24"
                }
                """;

        mockMvc.perform(post("/studyplan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(super.defaultCookie))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Title should not be empty"));
    }

    @Test
    void createdStudyPlanWithNoTitle() throws Exception {
        final String data = """
                {
                    "start_date": "2024-04-20",
                    "end_date": "2024-04-24"
                }
                """;

        mockMvc.perform(post("/studyplan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(super.defaultCookie))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Title should not be empty"));
    }

    @Test
    void createdStudyPlanWithInvalidStartDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "start_date": "15-2020",
                    "end_date": "2020-12-12"
                }
                """;

        mockMvc.perform(post("/studyplan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(super.defaultCookie))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid Date"));
    }

    @Test
    void createdStudyPlanWithInvalidEndDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "start_date": "2020-12-15",
                    "end_date": "12-2020"
                }
                """;

        mockMvc.perform(post("/studyplan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(super.defaultCookie))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid Date"));
    }

    @Test
    void createdStudyPlanWithNoStartDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "end_date": "2024-04-24"
                }
                """;

        mockMvc.perform(post("/studyplan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(super.defaultCookie))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Start date is required"));
    }

    @Test
    void createdStudyPlanWithNoEndDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "start_date": "2024-04-20"
                }
                """;

        mockMvc.perform(post("/studyplan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(super.defaultCookie))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("End date is required"));
    }
}