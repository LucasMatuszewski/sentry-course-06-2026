package com.policylab.api.quote;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    ProblemDetail problem =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "The enabled server-error scenario returned an intentional failure.");
    problem.setTitle("Demo server error");
    return problem;
  }
}

final class DemoServerException extends RuntimeException {
  DemoServerException() {
    super("Intentional PolicyLab server-error scenario");
  }
}
