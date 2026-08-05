package com.altencir.realtime.api;

import com.altencir.realtime.application.OperationNotFoundException;
import com.altencir.realtime.domain.InvalidOperationTransitionException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(InvalidOperationTransitionException.class)
    ProblemDetail invalidTransition(InvalidOperationTransitionException exception, HttpServletRequest request) {
        return problem(HttpStatus.CONFLICT, "Invalid operation transition", exception.getMessage(), request);
    }

    @ExceptionHandler(OperationNotFoundException.class)
    ProblemDetail notFound(OperationNotFoundException exception, HttpServletRequest request) {
        return problem(HttpStatus.NOT_FOUND, "Operation not found", exception.getMessage(), request);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    ProblemDetail invalidRequest(Exception exception, HttpServletRequest request) {
        return problem(HttpStatus.BAD_REQUEST, "Invalid request", exception.getMessage(), request);
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail, HttpServletRequest request) {
        var problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setProperty("correlationId", request.getAttribute(CorrelationIdFilter.ATTRIBUTE));
        return problem;
    }
}
