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
    @DisplayName("the rate for a single-use bottle is 25 cents")
    void rateInCents_singleUse_returns25() {
        assertThat(calculator.rateInCents(product(PackagingType.SINGLE_USE))).isEqualTo(25);
    }

    @Test
    @DisplayName("the rate for reusable glass is 8 cents")
    void rateInCents_reusableGlass_returns8() {
        assertThat(calculator.rateInCents(product(PackagingType.REUSABLE_GLASS)))
                .isEqualTo(8);
    }

    @Test
    @DisplayName("the rate for reusable plastic is 15 cents")
    void rateInCents_reusablePlastic_returns15() {
        assertThat(calculator.rateInCents(product(PackagingType.REUSABLE_PLASTIC)))
                .isEqualTo(15);
    }

    @Test
    @DisplayName("the rate for a crate is 150 cents")
    void rateInCents_crate_returns150() {
        assertThat(calculator.rateInCents(product(PackagingType.CRATE))).isEqualTo(150);
    }

    @Test
    @DisplayName("the rate for no-deposit packaging is 0 cents")
    void rateInCents_noDeposit_returns0() {
        assertThat(calculator.rateInCents(product(PackagingType.NO_DEPOSIT))).isEqualTo(0);
    }

    @Test
    @DisplayName("reusable glass deposit for one item is 8 cents")
    void depositInCents_reusableGlass_returns8() {
        assertThat(calculator.depositInCents(product(PackagingType.REUSABLE_GLASS), 1))
                .isEqualTo(8);
    }

    @Test
    @DisplayName("reusable plastic deposit for one item is 15 cents")
    void depositInCents_reusablePlastic_returns15() {
        assertThat(calculator.depositInCents(product(PackagingType.REUSABLE_PLASTIC), 1))
                .isEqualTo(15);
    }

    @Test
    @DisplayName("no-deposit packaging is worth 0 cents regardless of quantity")
    void depositInCents_noDeposit_returns0() {
        assertThat(calculator.depositInCents(product(PackagingType.NO_DEPOSIT), 6))
                .isEqualTo(0);
    }

    @Test
    @DisplayName("zero quantity is worth 0 cents")
    void depositInCents_zeroQuantity_returns0() {
        assertThat(calculator.depositInCents(product(PackagingType.SINGLE_USE), 0))
                .isEqualTo(0);
    }

    @Test
    @DisplayName("a negative quantity returns a negative deposit")
    void depositInCents_negativeQuantity_returnsNegativeDeposit() {
        assertThat(calculator.depositInCents(product(PackagingType.SINGLE_USE), -3))
                .isEqualTo(-75);
    }
}
