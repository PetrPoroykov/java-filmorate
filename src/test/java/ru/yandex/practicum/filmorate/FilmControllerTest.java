package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.DateIsNotValidException;
import ru.yandex.practicum.filmorate.exception.InvalidNumericValueException;
import ru.yandex.practicum.filmorate.exception.LengthOfFieldIsNotValidException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    FilmController filmController = new FilmController();

    @BeforeAll
     static void init() {
        FilmorateApplication.main(new String[]{});
    }

    @DisplayName("Должнен быть добавлен фильм")
    @Test
    void filmShouldBeAdded() {

        Film film = new Film();
        film.setName("Первый фильм");
        film.setDescription("Описание первого фильма");
        film.setReleaseDate((LocalDate.of(1995, 12, 28)));
        film.setDuration(31L);
        int countFilm = filmController.findAll().size();
        filmController.create(film);
        int countFilmNew = filmController.findAll().size();
        assertEquals(countFilm + 1, countFilmNew, "Неверное количество фильмов");
    }

    @DisplayName("Должно выбросываться исключение DateIsNotValidException когда дата релиза раньше 28 декабря 1895г.")
    @Test
    public void exceptionShouldBeDateIsNotValidException() {
        DateIsNotValidException exc = Assertions.assertThrows(DateIsNotValidException.class, () -> {
            Film film = new Film();
            film.setName("Первый фильм");
            film.setDescription("Описание первого фильма");
            film.setReleaseDate((LocalDate.of(1895, 12, 27)));
            film.setDuration(31L);
            filmController.create(film);
        });
        assertEquals("Дата релиза должна быть позже 28 декабря 1895 года", exc.getMessage());
    }

    @DisplayName("Должно выбросываться исключение ConditionsNotMetException " +
            "когда нет названия фильма.")
    @Test
    public void exceptionShouldBeThrownConditionsNotMetExceptionWhenNameIsempty() {
        ConditionsNotMetException exc = Assertions.assertThrows(ConditionsNotMetException.class, () -> {
            Film film = new Film();
            film.setName("");
            film.setDescription("Описание первого фильма");
            film.setReleaseDate((LocalDate.of(1895, 12, 27)));
            film.setDuration(31L);
            filmController.create(film);
        });
        assertEquals("Название не может быть пустым", exc.getMessage());
    }

    @DisplayName("Должно выбросываться исключение LengthOfFieldIsNotValidException " +
            "когда описание фильма больше 200 знаков.")
    @Test
    public void exceptionShouldBeThrownLengthOfFieldIsNotValidExceptionWhenDescriptionMoreThan200Characters() {
        LengthOfFieldIsNotValidException exc = Assertions.assertThrows(LengthOfFieldIsNotValidException.class, () -> {
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

    @DisplayName("Должно выбросываться исключение InvalidNumericValueException " +
            "когда продолжительность имеет отрицательное значение.")
    @Test
    public void exceptionShouldBeThrownInvalidNumericValueExceptionWhenDurationIsNegativeValue() {
        InvalidNumericValueException exc = Assertions.assertThrows(InvalidNumericValueException.class, () -> {
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
        int countFilm = filmController.findAll().size();
        filmController.create(film);
        int countFilmNew = filmController.findAll().size();
        assertEquals(countFilm + 1, countFilmNew, "Неверное количество фильмов");
        Film film1 = new Film();
        film1.setId(film.getId());
        film1.setName("ОБНОВЛЕННЫЙ фильм");
        film1.setDescription("Описание первого фильма");
        film1.setReleaseDate((LocalDate.of(1995, 12, 28)));
        film1.setDuration(31L);
        filmController.update(film1);
        assertEquals("ОБНОВЛЕННЫЙ фильм", film.getName(), "Данные не обновились");
        assertEquals(countFilm + 1, countFilmNew, "Неверное количество фильмов после обновления");
    }

}
