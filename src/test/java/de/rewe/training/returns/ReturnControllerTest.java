package de.rewe.training.returns;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Describes today's behaviour: the endpoint is reachable but has no logic.
 *
 * <p>The first test goes away once the endpoint is implemented — then new tests describe what it
 * should do.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ReturnControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("POST /api/returns returns 501 while the logic is missing")
    void calculateReturn_withoutImplementation_returns501() throws Exception {
        String body = """
                { "items": [ { "productId": "P-1001", "quantity": 6 } ] }
                """;

        mockMvc.perform(post("/api/returns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotImplemented());
    }

    @Test
    @DisplayName("POST /api/returns rejects an empty return")
    void calculateReturn_emptyItemList_returns400() throws Exception {
        String body = """
                { "items": [] }
                """;

        mockMvc.perform(post("/api/returns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
