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

    @Test
    @DisplayName("rateInCents for a single-use bottle is 25 cents")
    void rateInCents_singleUse_returns25() {
        assertThat(calculator.rateInCents(product(PackagingType.SINGLE_USE))).isEqualTo(25);
    }

    @Test
    @DisplayName("rateInCents for a reusable glass bottle is 8 cents")
    void rateInCents_reusableGlass_returns8() {
        assertThat(calculator.rateInCents(product(PackagingType.REUSABLE_GLASS)))
                .isEqualTo(8);
    }

    @Test
    @DisplayName("rateInCents for a reusable plastic bottle is 15 cents")
    void rateInCents_reusablePlastic_returns15() {
        assertThat(calculator.rateInCents(product(PackagingType.REUSABLE_PLASTIC)))
                .isEqualTo(15);
    }

    @Test
    @DisplayName("rateInCents for a crate is 150 cents")
    void rateInCents_crate_returns150() {
        assertThat(calculator.rateInCents(product(PackagingType.CRATE))).isEqualTo(150);
    }

    @Test
    @DisplayName("rateInCents for a no-deposit product is 0 cents")
    void rateInCents_noDeposit_returns0() {
        assertThat(calculator.rateInCents(product(PackagingType.NO_DEPOSIT))).isEqualTo(0);
    }

    @Test
    @DisplayName("three reusable glass bottles are worth 24 cents")
    void depositInCents_threeReusableGlassBottles_returns24() {
        assertThat(calculator.depositInCents(product(PackagingType.REUSABLE_GLASS), 3))
                .isEqualTo(24);
    }

    @Test
    @DisplayName("two reusable plastic bottles are worth 30 cents")
    void depositInCents_twoReusablePlasticBottles_returns30() {
        assertThat(calculator.depositInCents(product(PackagingType.REUSABLE_PLASTIC), 2))
                .isEqualTo(30);
    }

    @Test
    @DisplayName("a no-deposit product is worth 0 cents regardless of quantity")
    void depositInCents_noDepositProduct_returns0() {
        assertThat(calculator.depositInCents(product(PackagingType.NO_DEPOSIT), 4))
                .isEqualTo(0);
    }

    @Test
    @DisplayName("a quantity of zero is worth 0 cents")
    void depositInCents_zeroQuantity_returns0() {
        assertThat(calculator.depositInCents(product(PackagingType.SINGLE_USE), 0))
                .isEqualTo(0);
    }

    @Test
    @DisplayName("a negative quantity returns a negative deposit")
    void depositInCents_negativeQuantity_returnsNegativeDeposit() {
        assertThat(calculator.depositInCents(product(PackagingType.SINGLE_USE), -2))
                .isEqualTo(-50);
    }
}
