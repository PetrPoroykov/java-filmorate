package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;


public interface FilmStorage {
    void create(Film film);

    Film update(Film film);

    Film delete(Film film);

    Collection<Film> findAll();

    Film getById(Long id);

    Collection<Film> findBest(Integer count);
}
