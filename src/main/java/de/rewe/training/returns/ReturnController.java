package de.rewe.training.returns;

import de.rewe.training.catalog.ProductRepository;
import de.rewe.training.deposit.DepositCalculator;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Deposit return.
 *
 * <p>The endpoint exists, the logic does not. That is the exercise — see README.md.
 */
@RestController
@RequestMapping("/api/returns")
public class ReturnController {

    private final ProductRepository products;
    private final DepositCalculator calculator;

    public ReturnController(ProductRepository products, DepositCalculator calculator) {
        this.products = products;
        this.calculator = calculator;
    }

    @PostMapping
    public ReturnReceipt calculateReturn(@Valid @RequestBody ReturnRequest request) {
        throw new UnsupportedOperationException("Deposit return is not implemented yet");
    }
}
