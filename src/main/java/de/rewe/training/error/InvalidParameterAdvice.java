package de.rewe.training.error;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Turns a query parameter Spring cannot convert into a problem detail.
 *
 * <p>Spring answers these with a 400 and an empty body, which tells a caller who mistyped a value
 * nothing at all. For an enum the accepted spellings are known, so they go into the detail.
 */
@RestControllerAdvice
public class InvalidParameterAdvice {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handle(MethodArgumentTypeMismatchException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detailOf(exception));
        problem.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
        return problem;
    }

    private String detailOf(MethodArgumentTypeMismatchException exception) {
        String detail = "Invalid value '%s' for parameter '%s'".formatted(exception.getValue(), exception.getName());
        Class<?> expected = expectedEnumOf(exception);
        if (expected == null) {
            return detail + ".";
        }
        return detail + ". Expected one of: "
                + Arrays.stream(expected.getEnumConstants())
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
    }

    /**
     * The enum the value was meant to become. It is not on the exception itself: for a repeatable
     * parameter the declared type is the collection, and only the nested conversion failure knows
     * the element type.
     */
    private Class<?> expectedEnumOf(Throwable failure) {
        for (Throwable cause = failure; cause != null; cause = cause.getCause()) {
            if (cause instanceof ConversionFailedException conversion) {
                Class<?> target = conversion.getTargetType().getType();
                if (target.isEnum()) {
                    return target;
                }
            }
        }
        return null;
    }
}
