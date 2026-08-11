package de.rewe.training.returns;

import java.util.List;

/**
 * The receipt printed by the return machine.
 *
 * @param lines one line per returned product
 * @param totalDepositCents deposit to be paid out, in cents
 */
public record ReturnReceipt(List<Line> lines, int totalDepositCents) {

    /**
     * One line on the receipt.
     *
     * @param productId article number
     * @param productName label as printed on the receipt
     * @param quantity number of items returned
     * @param depositPerItemCents deposit per item, in cents
     * @param depositCents deposit for this line, in cents
     */
    public record Line(String productId, String productName, int quantity, int depositPerItemCents, int depositCents) {}
}
