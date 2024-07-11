package ru.yandex.practicum.filmorate.exception;

public class DateIsNotValidException extends RuntimeException {
    public DateIsNotValidException(String message) {
        super(message);
    }
}