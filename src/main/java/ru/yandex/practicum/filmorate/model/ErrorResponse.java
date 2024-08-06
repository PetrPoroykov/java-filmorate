package ru.yandex.practicum.filmorate.model;

public class ErrorResponse {
    private final String message;

    public ErrorResponse(String error) {
        this.message = error;
    }

    public String getMessage() {
        return message;
    }
}
