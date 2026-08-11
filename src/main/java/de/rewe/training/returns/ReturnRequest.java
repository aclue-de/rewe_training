package de.rewe.training.returns;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/** What a customer hands back at the return machine. */
public record ReturnRequest(@NotEmpty List<@Valid Item> items) {

    /**
     * One position of a return.
     *
     * @param productId article number of the returned product
     * @param quantity number of items returned
     */
    public record Item(@NotBlank String productId, int quantity) {}
}
