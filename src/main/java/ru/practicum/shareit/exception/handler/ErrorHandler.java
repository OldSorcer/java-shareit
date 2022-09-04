package ru.practicum.shareit.exception.handler;

import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.EntityCreateException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidArgumentException;

import javax.validation.ValidationException;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handle(EntityNotFoundException exc) {
        return Map.of("Возникла ошибка", exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handle(InvalidArgumentException exc) {
        return Map.of("Возникла ошибка", exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handle(EntityCreateException exc) {
        return Map.of("Возникла ошибка", exc.getMessage());
    }
}