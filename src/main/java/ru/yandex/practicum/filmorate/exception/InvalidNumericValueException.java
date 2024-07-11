package ru.yandex.practicum.filmorate.exception;

public class InvalidNumericValueException extends RuntimeException{
    public InvalidNumericValueException(String message){
        super(message);
    }
}
