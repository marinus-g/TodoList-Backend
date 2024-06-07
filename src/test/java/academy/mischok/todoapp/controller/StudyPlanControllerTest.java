package academy.mischok.todoapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class StudyPlanControllerTest extends AuthenticatedBaseControllerTest {

    @Test
    void testCreateValidStudyPlan() throws Exception {

        final ObjectMapper objectMapper = new ObjectMapper();
        String contentAsString = mockMvc.perform(get("/studyplan")
                        .cookie(super.defaultCookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<?> jsonArray = objectMapper.readValue(contentAsString, List.class);
        int length = jsonArray.size();
        final String data = """
                {
                    "title": "Test Studyplan",
                    "start_date": "20-04-2024",
                    "end_date": "24-04-2024",
                }
                """;
        mockMvc.perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("/studyplan/")));
        mockMvc.perform(get("/studyplan").cookie(super.defaultCookie))
                .andExpect(jsonPath("$", hasSize(length + 1)));
    }

    @Test
    void createdStudyPlanWithShortTitle() throws Exception {
        final String data = """
                {
                    "title": "T",
                    "start_date": "20-04-2024",
                    "end_date": "24-04-2024",
                }
                """;

        mockMvc.perform(post("/studyplan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Title too short"));

    }

    @Test
    void createdStudyPlanWithBlankTitle() throws Exception {
        final String data = """
                {
                    "title": "",
                    "start_date": "20-04-2024",
                    "end_date": "24-04-2024",
                }
                """;

        mockMvc.perform(post("/studyplan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Title should not be empty"));

    }

    @Test
    void createdStudyPlanWithNoTitle() throws Exception {
        final String data = """
                {
                    "start_date": "20-04-2024",
                    "end_date": "24-04-2024",
                }
                """;

        mockMvc.perform(post("/studyplan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                        .cookie(super.defaultCookie)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Title should not be empty"));

    }

    @Test
    void createdStudyPlanWithInvalidStartDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "start_date": "15-2020",
                    "end_date": "12-12-2020",
                }
                """;

        mockMvc.perform(post("/studyplan")
                        .cookie(super.defaultCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid Date"));

    }

    @Test
    void createdStudyPlanWithInvalidEndDate() throws Exception {
        final String data = """
                {
                    "title": "test",
                    "start_date": "15-12-2020",
                    "end_date": "12-2020",
                }
                """;

        mockMvc.perform(post("/studyplan")
                        .cookie(super.defaultCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Invalid Date"));

    }

    //@Test
    void createdStudyPlanWithNoStartDate() throws Exception {
    }

    //@Test
    void createdStudyPlanWithNoEndDate() throws Exception {
    }
}
