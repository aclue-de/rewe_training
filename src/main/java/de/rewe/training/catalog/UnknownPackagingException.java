package de.rewe.training.catalog;

import java.util.Arrays;
import java.util.stream.Collectors;

/** Thrown when a {@code packaging} filter value does not match any {@link PackagingType}. */
class UnknownPackagingException extends RuntimeException {

    UnknownPackagingException(String value) {
        super("Unknown packaging value '%s'. Allowed values: %s".formatted(value, allowedValues()));
    }

    private static String allowedValues() {
        return Arrays.stream(PackagingType.values()).map(Enum::name).collect(Collectors.joining(", "));
    }
}
