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
    @DisplayName("GET /api/products?packaging=CRATE returns only crates")
    void findAll_singlePackagingFilter_returnsMatchingProducts() throws Exception {
        mockMvc.perform(get("/api/products").param("packaging", "CRATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("P-1004"));
    }

    @Test
    @DisplayName("GET /api/products?packaging=CRATE&packaging=SINGLE_USE returns both in one call")
    void findAll_twoPackagingFilters_returnsUnionOfMatchingProducts() throws Exception {
        mockMvc.perform(get("/api/products").param("packaging", "CRATE", "SINGLE_USE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value("P-1001"))
                .andExpect(jsonPath("$[1].id").value("P-1004"))
                .andExpect(jsonPath("$[2].id").value("P-1006"));
    }

    @Test
    @DisplayName("GET /api/products?packaging=FOO returns 400 with a problem detail naming the allowed values")
    void findAll_unknownPackagingFilter_returns400WithProblemDetail() throws Exception {
        mockMvc.perform(get("/api/products").param("packaging", "FOO"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(
                        jsonPath("$.detail")
                                .value(
                                        "Unknown packaging value 'FOO'. Allowed values: SINGLE_USE, REUSABLE_GLASS, REUSABLE_PLASTIC, CRATE, NO_DEPOSIT"));
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
