package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFoundHandler(final NotFoundException e) {
        log.warn("404 {}", e.toString());
        return Map.of("404 {}", e.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> validationException(final ValidationException e) {
        log.warn("409 {}", e.toString());
        return Map.of("409 {}", e.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // Было INTERNAL_SERVER_ERROR
    public Map<String, String> throwableHandler(final Throwable e) {
        log.warn("400 {}", e.toString());
        return Map.of("400 {}", e.toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // new
    public Map<String, String> availableHandler(final AvailableCheckException e) {
        log.warn("400 {}", e.toString());
        return Map.of("400 {}", e.toString());
    }

}
