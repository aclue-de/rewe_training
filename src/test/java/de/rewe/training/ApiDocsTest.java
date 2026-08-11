package de.rewe.training;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/** Guards the API documentation setup — it is the entry point for everyone using this project. */
@SpringBootTest
@AutoConfigureMockMvc
class ApiDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("the generated OpenAPI document lists all three endpoints")
    void apiDocs_generated_containsAllEndpoints() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("rewe-training"))
                .andExpect(jsonPath("$.paths['/api/products']").exists())
                .andExpect(jsonPath("$.paths['/api/products/{id}']").exists())
                .andExpect(jsonPath("$.paths['/api/returns']").exists());
    }

    @Test
    @DisplayName("the documentation shows the three endpoints and nothing else")
    void apiDocs_generated_containsNothingBesidesTheApi() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths.length()").value(3))
                .andExpect(jsonPath("$.paths['/error']").doesNotExist());
    }

    @Test
    @DisplayName("the root path leads to the Swagger UI")
    void root_opened_redirectsToSwaggerUi() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().is3xxRedirection());
    }
}
