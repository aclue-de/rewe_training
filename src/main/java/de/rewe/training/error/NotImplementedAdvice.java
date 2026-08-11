package de.rewe.training.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Turns "not implemented yet" into a clean 501 instead of a stack trace. */
@RestControllerAdvice
public class NotImplementedAdvice {

    @ExceptionHandler(UnsupportedOperationException.class)
    public ProblemDetail handle(UnsupportedOperationException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_IMPLEMENTED, exception.getMessage());
    }
}
