package de.rewe.training.returns;

import de.rewe.training.catalog.Product;
import de.rewe.training.catalog.ProductRepository;
import de.rewe.training.deposit.DepositCalculator;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
        List<ReturnReceipt.Line> lines =
                request.items().stream().map(this::lineFor).toList();
        int totalDepositCents =
                lines.stream().mapToInt(ReturnReceipt.Line::depositCents).sum();
        return new ReturnReceipt(lines, totalDepositCents);
    }

    private ReturnReceipt.Line lineFor(ReturnRequest.Item item) {
        Product product = products.findById(item.productId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "No product with id " + item.productId()));
        int depositPerItemCents = calculator.rateInCents(product);
        int depositCents = calculator.depositInCents(product, item.quantity());
        return new ReturnReceipt.Line(product.id(), product.name(), item.quantity(), depositPerItemCents, depositCents);
    }
}
