package de.rewe.training.deposit;

import de.rewe.training.catalog.PackagingType;
import de.rewe.training.catalog.Product;
import java.util.Map;
import org.springframework.stereotype.Component;

/** Calculates the deposit for a product and a given quantity. */
@Component
public class DepositCalculator {

    private static final Map<PackagingType, Integer> RATES_IN_CENTS = Map.of(
            PackagingType.SINGLE_USE, 25,
            PackagingType.REUSABLE_GLASS, 8,
            PackagingType.REUSABLE_PLASTIC, 15,
            PackagingType.CRATE, 150,
            PackagingType.NO_DEPOSIT, 0);

    /** Deposit for a single item of this product, in cents. */
    public int rateInCents(Product product) {
        return RATES_IN_CENTS.getOrDefault(product.packaging(), 0);
    }

    /** Deposit for the given quantity of this product, in cents. */
    public int depositInCents(Product product, int quantity) {
        return rateInCents(product) * quantity;
    }
}
