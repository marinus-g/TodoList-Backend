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
    void testCreateStudyPlan() throws Exception {

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
}
