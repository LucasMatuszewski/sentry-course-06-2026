package com.policylab.api.quote;

import io.sentry.Sentry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the PolicyLab API.
 *
 * <p>Teaching point: when a Spring app installs a {@code @RestControllerAdvice}
 * that catches an exception, {@code SentryExceptionResolver} sees the exception
 * as "handled" and does <em>not</em> create a Sentry event. The exception
 * silently disappears from observability. We must explicitly call
 * {@code Sentry.captureException} from inside the handler.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

  static ProblemDetail invalidQuoteRequest() {
    ProblemDetail problem =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "One or more quote fields are invalid.");
    problem.setTitle("Invalid quote request");
    return problem;
  }

  static ProblemDetail demoValidationError() {
    ProblemDetail problem =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, "The enabled validation scenario returned an expected error.");
    problem.setTitle("Demo validation error");
    return problem;
  }

  @ExceptionHandler(DemoServerException.class)
  ProblemDetail demoServerError(DemoServerException exception) {
    // Capture to Sentry BEFORE returning the user-facing response.
    // Without this line the @RestControllerAdvice eats the exception and
    // SentryExceptionResolver never fires -> the 500 vanishes from
    // observability even though the user sees a perfectly clean response.
    Sentry.captureException(exception);

    ProblemDetail problem =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "The enabled server-error scenario returned an intentional failure.");
    problem.setTitle("Demo server error");
    return problem;
  }

  /**
   * Catch-all for unexpected exceptions. Without this, Spring still returns a
   * 500 via {@link SentryExceptionResolver}, but having an explicit handler
   * lets us shape the response while still capturing every error.
   */
  @ExceptionHandler(Exception.class)
  ProblemDetail unexpectedError(Exception exception) {
    Sentry.captureException(exception);
    ProblemDetail problem =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred. The incident has been reported.");
    problem.setTitle("Internal server error");
    return problem;
  }
}

final class DemoServerException extends RuntimeException {
  DemoServerException() {
    super("Intentional PolicyLab server-error scenario");
  }
}
