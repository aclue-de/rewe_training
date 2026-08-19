package de.rewe.training.catalog;

import static org.hamcrest.Matchers.containsString;
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
    void findAll_onePackagingType_returnsOnlyThatType() throws Exception {
        mockMvc.perform(get("/api/products").param("packaging", "CRATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("P-1004"));
    }

    @Test
    @DisplayName("GET /api/products with several packaging types returns their union, in seed order")
    void findAll_severalPackagingTypes_returnsTheUnion() throws Exception {
        mockMvc.perform(get("/api/products").param("packaging", "CRATE").param("packaging", "SINGLE_USE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value("P-1001"))
                .andExpect(jsonPath("$[1].id").value("P-1004"))
                .andExpect(jsonPath("$[2].id").value("P-1006"));
    }

    @Test
    @DisplayName("GET /api/products with an unknown packaging type returns 400 as a problem detail")
    void findAll_unknownPackagingType_returns400WithProblemDetail() throws Exception {
        mockMvc.perform(get("/api/products").param("packaging", "BOTTLE_CRATE"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.detail").value(containsString("BOTTLE_CRATE")))
                .andExpect(jsonPath("$.detail").value(containsString("CRATE")));
    }

    @Test
    @DisplayName("the packaging type is spelled as in the enum — lower case is a 400")
    void findAll_lowerCasePackagingType_returns400() throws Exception {
        mockMvc.perform(get("/api/products").param("packaging", "crate")).andExpect(status().isBadRequest());
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
