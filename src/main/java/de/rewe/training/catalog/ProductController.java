package de.rewe.training.catalog;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository products;

    public ProductController(ProductRepository products) {
        this.products = products;
    }

    @GetMapping
    public List<Product> findAll(@RequestParam(name = "packaging", required = false) List<String> packaging) {
        Set<PackagingType> types = parsePackaging(packaging);
        return types.isEmpty() ? products.findAll() : products.findAll(types);
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable String id) {
        return products.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No product with id " + id));
    }

    @ExceptionHandler(UnknownPackagingException.class)
    public ProblemDetail handleUnknownPackaging(UnknownPackagingException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problem.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return problem;
    }

    /** A blank value behaves like it was never given; an unrecognized one is rejected. */
    private static Set<PackagingType> parsePackaging(List<String> rawValues) {
        if (rawValues == null) {
            return Set.of();
        }
        Set<PackagingType> types = EnumSet.noneOf(PackagingType.class);
        for (String rawValue : rawValues) {
            if (rawValue.isEmpty()) {
                continue;
            }
            types.add(toPackagingType(rawValue));
        }
        return types;
    }

    private static PackagingType toPackagingType(String rawValue) {
        try {
            return PackagingType.valueOf(rawValue);
        } catch (IllegalArgumentException e) {
            throw new UnknownPackagingException(rawValue);
        }
    }
}
