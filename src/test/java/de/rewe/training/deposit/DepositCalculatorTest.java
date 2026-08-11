package de.rewe.training.deposit;

import static org.assertj.core.api.Assertions.assertThat;

import de.rewe.training.catalog.PackagingType;
import de.rewe.training.catalog.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DepositCalculatorTest {

    private final DepositCalculator calculator = new DepositCalculator();

    private static Product product(PackagingType packaging) {
        return new Product("P-0000", "Test product", 100, packaging);
    }

    @Test
    @DisplayName("a single-use bottle is worth 25 cents")
    void depositInCents_singleUseBottle_returns25() {
        assertThat(calculator.depositInCents(product(PackagingType.SINGLE_USE), 1))
                .isEqualTo(25);
    }

    @Test
    @DisplayName("six single-use bottles are worth 150 cents")
    void depositInCents_sixSingleUseBottles_returns150() {
        assertThat(calculator.depositInCents(product(PackagingType.SINGLE_USE), 6))
                .isEqualTo(150);
    }

    @Test
    @DisplayName("a crate is worth 150 cents")
    void depositInCents_crate_returns150() {
        assertThat(calculator.depositInCents(product(PackagingType.CRATE), 1)).isEqualTo(150);
    }
}
