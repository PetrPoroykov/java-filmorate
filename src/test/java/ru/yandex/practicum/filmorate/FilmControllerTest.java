package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FilmControllerTest {
    private InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    private FilmService filmService = new FilmService();
    private UserStorage userStorage = new InMemoryUserStorage();
    private FilmController filmController = new FilmController(inMemoryFilmStorage, filmService,userStorage);

    @DisplayName("Должнен быть добавлен фильм")
    @Test
    void filmShouldBeAdded() {

        Film film = new Film();
        film.setName("Первый фильм");
        film.setDescription("Описание первого фильма");
        film.setReleaseDate((LocalDate.of(1995, 12, 28)));
        film.setDuration(31L);
        int countFilm = inMemoryFilmStorage.getFilms().size();
        inMemoryFilmStorage.create(film);
        int countFilmNew = inMemoryFilmStorage.getFilms().size();
        assertEquals(countFilm + 1, countFilmNew, "Неверное количество фильмов");
    }

    @DisplayName("Должно выбросываться исключение ValidationException когда дата релиза раньше 28 декабря 1895г.")
    @Test
    public void shouldBeThrownValidationExceptionWhenDateIsNotValid() {
        ValidationException exc = Assertions.assertThrows(ValidationException.class, () -> {
            Film film = new Film();
            film.setName("Первый фильм");
            film.setDescription("Описание первого фильма");
            film.setReleaseDate((LocalDate.of(1895, 12, 27)));
            film.setDuration(31L);
            filmController.create(film);

        });
        assertEquals("Дата релиза должна быть позже 28 декабря 1895 года", exc.getMessage());
    }

    @DisplayName("Должно выбросываться исключение ValidationExceptionn " +
            "когда нет названия фильма.")
    @Test
    public void shouldBeThrownValidationExceptionWhenNameIsempty() {
        ValidationException exc = Assertions.assertThrows(ValidationException.class, () -> {
            Film film = new Film();
            film.setName("");
            film.setDescription("Описание первого фильма");
            film.setReleaseDate((LocalDate.of(1895, 12, 27)));
            film.setDuration(31L);
            filmController.create(film);
        });
        assertEquals("Название не может быть пустым", exc.getMessage());
    }

    @DisplayName("Должно выбросываться исключение ValidationExcepti " +
            "когда описание фильма больше 200 знаков.")
    @Test
    public void shouldBeThrownValidationExceptionWhenDescriptionMoreThan200Characters() {
        ValidationException exc = Assertions.assertThrows(ValidationException.class, () -> {
            Film film = new Film();
            StringBuilder sbld = new StringBuilder("a");
            for (int i = 1; i < 201; i++) {
                sbld = sbld.append("b");
            }
            film.setName("Любой фильм");
            film.setDescription(String.valueOf(sbld));
            film.setReleaseDate((LocalDate.of(1995, 12, 27)));
            film.setDuration(31L);
            filmController.create(film);
        });
        assertEquals("Длина описания превышает 200 знаков", exc.getMessage());
    }

    @DisplayName("Должно выбросываться исключение ValidationException " +
            "когда продолжительность имеет отрицательное значение.")
    @Test
    public void shouldBeThrownValidationExceptionWhenDurationIsNegativeValue() {
        ValidationException exc = Assertions.assertThrows(ValidationException.class, () -> {
            Film film = new Film();
            StringBuilder sbld = new StringBuilder("a");
            for (int i = 1; i < 201; i++) {
                sbld = sbld.append("b");
            }
            film.setName("Любой фильм");
            film.setDescription("Описание");
            film.setReleaseDate((LocalDate.of(1995, 12, 27)));
            film.setDuration(-1L);
            filmController.create(film);
        });
        assertEquals("Продолжительность фильма должна быть положительным числом", exc.getMessage());
    }

    @DisplayName("Фильм должнен быть оновлен")
    @Test
    void filmShouldBeUpdata() {

        Film film = new Film();
        film.setName("Первый фильм");
        film.setDescription("Описание первого фильма");
        film.setReleaseDate((LocalDate.of(1995, 12, 28)));
        film.setDuration(31L);
        int countFilm = inMemoryFilmStorage.getFilms().size();;
        inMemoryFilmStorage.create(film);
        int countFilmNew = inMemoryFilmStorage.getFilms().size();
        assertEquals(countFilm + 1, countFilmNew, "Неверное количество фильмов");
        Film film1 = new Film();
        film1.setId(film.getId());
        film1.setName("ОБНОВЛЕННЫЙ фильм");
        film1.setDescription("Описание первого фильма");
        film1.setReleaseDate((LocalDate.of(1995, 12, 28)));
        film1.setDuration(31L);
        inMemoryFilmStorage.update(film1);
        assertEquals("ОБНОВЛЕННЫЙ фильм", film.getName(), "Данные не обновились");
        assertEquals(countFilm + 1, countFilmNew, "Неверное количество фильмов после обновления");
    }
}
