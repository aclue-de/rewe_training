package de.rewe.training.returns;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReturnControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    @DisplayName("POST /api/returns builds one line per item, in request order, with the total")
    void calculateReturn_multipleItems_returnsReceiptInRequestOrder() throws Exception {
        String body =
                """
                { "items": [
                    { "productId": "P-1001", "quantity": 6 },
                    { "productId": "P-1004", "quantity": 1 }
                ] }
                """;

        mockMvc.perform(post("/api/returns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lines[0].productId").value("P-1001"))
                .andExpect(jsonPath("$.lines[0].productName").value("Sparkling water 0.5 l"))
                .andExpect(jsonPath("$.lines[0].quantity").value(6))
                .andExpect(jsonPath("$.lines[0].depositPerItemCents").value(25))
                .andExpect(jsonPath("$.lines[0].depositCents").value(150))
                .andExpect(jsonPath("$.lines[1].productId").value("P-1004"))
                .andExpect(jsonPath("$.lines[1].productName").value("Lager crate 20 x 0.5 l"))
                .andExpect(jsonPath("$.lines[1].quantity").value(1))
                .andExpect(jsonPath("$.lines[1].depositPerItemCents").value(150))
                .andExpect(jsonPath("$.lines[1].depositCents").value(150))
                .andExpect(jsonPath("$.totalDepositCents").value(300));
    }

    @Test
    @DisplayName("POST /api/returns puts a no-deposit product on the receipt at 0 cents")
    void calculateReturn_noDepositProduct_returnsZeroCents() throws Exception {
        String body = """
                { "items": [ { "productId": "P-1005", "quantity": 2 } ] }
                """;

        mockMvc.perform(post("/api/returns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lines[0].depositPerItemCents").value(0))
                .andExpect(jsonPath("$.lines[0].depositCents").value(0))
                .andExpect(jsonPath("$.totalDepositCents").value(0));
    }

    @Test
    @DisplayName("POST /api/returns rejects an unknown product id with 404")
    void calculateReturn_unknownProductId_returns404() throws Exception {
        String body = """
                { "items": [ { "productId": "P-9999", "quantity": 1 } ] }
                """;

        mockMvc.perform(post("/api/returns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/returns rejects a quantity of zero with 400")
    void calculateReturn_zeroQuantity_returns400() throws Exception {
        String body = """
                { "items": [ { "productId": "P-1001", "quantity": 0 } ] }
                """;

        mockMvc.perform(post("/api/returns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/returns rejects a negative quantity with 400")
    void calculateReturn_negativeQuantity_returns400() throws Exception {
        String body =
                """
                { "items": [ { "productId": "P-1001", "quantity": -1 } ] }
                """;

        mockMvc.perform(post("/api/returns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/returns rejects invalid quantity before any product lookup runs")
    void calculateReturn_invalidQuantityAndUnknownProductId_returns400() throws Exception {
        String body =
                """
                { "items": [
                    { "productId": "P-1001", "quantity": 0 },
                    { "productId": "P-9999", "quantity": 1 }
                ] }
                """;

        mockMvc.perform(post("/api/returns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }
}
