package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private  static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

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
        film.setId(getNextId());

        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: " + film.getName() + ", фильму  присвоен ID: " + film.getId());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {

        if (newFilm.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getName() == null || newFilm.getName().isBlank()) {
                throw new ConditionsNotMetException("Название не может быть пустым");
            }
            if (newFilm.getDescription().length() > 200) {
                throw new LengthOfFieldIsNotValidException("Длина описания превышает 200 знаков");
            }
            if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                throw new DateIsNotValidException("Дата релиза должна быть позже 28 декабря 1895 года");
            }
            if (newFilm.getDuration() <= 0) {
                throw new InvalidNumericValueException("Продолжительность фильма должна быть положительным числом");
            }
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Данные фильма с ID " + oldFilm.getId() + " обновлены");
            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
