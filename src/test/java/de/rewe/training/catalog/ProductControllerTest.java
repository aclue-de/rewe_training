package de.rewe.training.catalog;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/products returns the whole assortment")
    void findAll_noFilter_returnsAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(8))
                .andExpect(jsonPath("$[0].id").value("P-1001"));
    }

    @Test
    @DisplayName("GET /api/products/{id} returns the matching product")
    void findById_knownId_returnsProduct() throws Exception {
        mockMvc.perform(get("/api/products/P-1003"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lager 0.5 l"))
                .andExpect(jsonPath("$.priceCents").value(99))
                .andExpect(jsonPath("$.packaging").value("REUSABLE_GLASS"));
    }

    @Test
    @DisplayName("GET /api/products/{id} returns 404 for an unknown id")
    void findById_unknownId_returns404() throws Exception {
        mockMvc.perform(get("/api/products/P-9999")).andExpect(status().isNotFound());
    }
}
