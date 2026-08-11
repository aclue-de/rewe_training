package de.rewe.training.catalog;

/**
 * A product in the assortment.
 *
 * @param id article number, for example "P-1001"
 * @param name label as printed on the receipt
 * @param priceCents sales price in cents, deposit not included
 * @param packaging packaging type, determines the deposit rate
 */
public record Product(String id, String name, int priceCents, PackagingType packaging) {}
