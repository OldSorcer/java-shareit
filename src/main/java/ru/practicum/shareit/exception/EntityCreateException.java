package ru.practicum.shareit.exception;

public class EntityCreateException extends RuntimeException {
    public EntityCreateException(String message) {
        super(message);
    }
}