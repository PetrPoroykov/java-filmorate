package ru.yandex.practicum.filmorate.exception;

public class LengthOfFieldIsNotValidException extends RuntimeException{
    public LengthOfFieldIsNotValidException(String message){
        super(message);
    }
}
