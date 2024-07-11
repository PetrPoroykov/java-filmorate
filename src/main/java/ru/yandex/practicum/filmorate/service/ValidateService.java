package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DateIsNotValidException;
import ru.yandex.practicum.filmorate.exception.InvalidNumericValueException;
import ru.yandex.practicum.filmorate.exception.LengthOfFieldIsNotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class ValidateService {

    public static void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ConditionsNotMetException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new LengthOfFieldIsNotValidException("Длина описания превышает 200 знаков");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new DateIsNotValidException("Дата релиза должна быть позже 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new InvalidNumericValueException("Продолжительность фильма должна быть положительным числом");
        }
    }

    public static void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || user.getEmail().indexOf('@') == -1) {
            throw new ConditionsNotMetException("Email не может быть пустым и должен содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || !(user.getLogin().indexOf(' ') == -1)) {
            throw new ConditionsNotMetException("Login не может быть пустым и  не должен  содержать пробел");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new DateIsNotValidException("Дата рождения не может быть в будущем");
        }
    }
}
