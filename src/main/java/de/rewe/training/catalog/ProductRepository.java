package de.rewe.training.catalog;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * Access to the assortment.
 *
 * <p>In-memory on purpose: this project is about the workflow, not about persistence. Swap the seed
 * for a database and the rest of the code stays as it is.
 */
@Repository
public class ProductRepository {

    private static final List<Product> SEED = List.of(
            new Product("P-1001", "Sparkling water 0.5 l", 79, PackagingType.SINGLE_USE),
            new Product("P-1002", "Apple spritzer 1.0 l", 149, PackagingType.REUSABLE_PLASTIC),
            new Product("P-1003", "Lager 0.5 l", 99, PackagingType.REUSABLE_GLASS),
            new Product("P-1004", "Lager crate 20 x 0.5 l", 1799, PackagingType.CRATE),
            new Product("P-1005", "Whole milk 1 l", 129, PackagingType.NO_DEPOSIT),
            new Product("P-1006", "Cola 1.5 l", 189, PackagingType.SINGLE_USE),
            new Product("P-1007", "Yoghurt 500 g", 99, PackagingType.NO_DEPOSIT),
            new Product("P-1008", "Orange juice 0.7 l", 229, PackagingType.REUSABLE_GLASS));

    public List<Product> findAll() {
        return SEED;
    }

    public Optional<Product> findById(String id) {
        return SEED.stream().filter(product -> product.id().equals(id)).findFirst();
    }
}
