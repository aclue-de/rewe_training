package de.rewe.training.error;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Answers the servlet error dispatch with a problem detail instead of Spring's Whitelabel Error
 * Page. Without this, any unmapped path renders an HTML fallback that says nothing useful.
 *
 * <p>Hidden from the API documentation: {@code /error} is a servlet mechanism, not part of the API.
 * It also maps every HTTP method, so it would otherwise fill the docs with eight entries.
 */
@Hidden
@RestController
public class ApiErrorController implements ErrorController {

    @RequestMapping("/error")
    public ProblemDetail handleError(HttpServletRequest request) {
        HttpStatus status = statusOf(request);
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(status.getReasonPhrase());

        Object path = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        if (path != null) {
            problem.setProperty("path", path);
        }
        if (status == HttpStatus.NOT_FOUND) {
            problem.setDetail("Unknown path. The API documentation is at /");
        }
        return problem;
    }

    private HttpStatus statusOf(HttpServletRequest request) {
        if (request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) instanceof Integer code) {
            HttpStatus status = HttpStatus.resolve(code);
            if (status != null) {
                return status;
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
