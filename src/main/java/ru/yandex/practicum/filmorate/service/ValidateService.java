package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class ValidateService {

    public static void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Длина описания превышает 200 знаков");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза должна быть позже 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }

    public static void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || user.getEmail().indexOf('@') == -1) {
            throw new ValidationException("Email не может быть пустым и должен содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || !(user.getLogin().indexOf(' ') == -1)) {
            throw new ValidationException("Login не может быть пустым и  не должен  содержать пробел");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
