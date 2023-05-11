package ru.practicum.shareit.exception;

public class NotExistInDataBase extends RuntimeException {

    public NotExistInDataBase(String message) {
        super(message);
    }
}