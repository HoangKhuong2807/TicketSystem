package com.wc2026.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("https://wc2026-tickets.com/errors/resource-not-found"));
        pd.setTitle("Resource Not Found");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.resolve(ex.getStatus()), ex.getMessage());
        pd.setType(URI.create("https://wc2026-tickets.com/errors/" + ex.getErrorType()));
        pd.setTitle("Business Rule Violation");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        record FieldErrorItem(String field, String message, Object rejectedValue) {}

        List<FieldErrorItem> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new FieldErrorItem(fe.getField(), fe.getDefaultMessage(), fe.getRejectedValue()))
                .collect(Collectors.toList());

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setType(URI.create("https://wc2026-tickets.com/errors/validation-error"));
        pd.setTitle("Validation Failed");
        pd.setDetail("Dữ liệu gửi lên không hợp lệ (" + errors.size() + " lỗi)");
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneral(Exception ex) {
        log.error("Unhandled exception: ", ex);
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setType(URI.create("https://wc2026-tickets.com/errors/internal-server-error"));
        pd.setTitle("Internal Server Error");
        pd.setDetail("Lỗi hệ thống không xác định. Vui lòng liên hệ Admin.");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}
