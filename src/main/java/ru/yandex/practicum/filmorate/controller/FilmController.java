package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Component
@RestController
@RequestMapping("/films")
public class FilmController {

    final FilmStorage filmStorage;
    final FilmService filmService;
    final UserStorage userStorage;

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.userStorage = userStorage;
    }


    @GetMapping
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        ValidateService.validateFilm(film);
        filmStorage.create(film);
        log.info("Добавлен новый фильм: " + film.getName() + ", фильму  присвоен ID: " + film.getId());
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Данные фильма с ID " + newFilm.getId() + " обновлены");
        return filmStorage.update(newFilm);
    }

    @DeleteMapping
    public Film delete(@RequestBody Film film) {
        log.info("Данные фильма с ID " + film.getId() + " удалены");
        return filmStorage.delete(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable final Long id, @PathVariable final Long userId) {
        filmService.addLike(filmStorage.getById(id), userStorage.getById(userId));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable final Long id, @PathVariable final Long userId) {
        filmService.deleteLike(filmStorage.getById(id), userStorage.getById(userId));
    }

    @GetMapping("/popular")
    public Collection<Film> findBest(@RequestParam(defaultValue = "10") Integer count) {
        return filmStorage.findBest(count);
    }
}
