package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.LikesComparator;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    LikesComparator likesComparator = new LikesComparator();


    @Override
    public void create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
    }

    @Override
    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            ValidateService.validateFilm(newFilm);

            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
//            log.info("Данные фильма с ID " + oldFilm.getId() + " обновлены");
            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    @Override
    public Film delete(Film film) {
        if (film.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (films.containsValue(film)) {
            films.remove(film);
            return film;
        }
        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
    }

    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film getById(Long id) {
        return films.values().stream().filter(film -> film.getId() == id)
                .findFirst().orElseThrow(() -> new NotFoundException("Фильм с номером " + id + " не найден"));
    }

    public Collection<Film> findBest(Integer count) {
        List<Film> list = films.values().stream()
                .sorted(likesComparator)
                .collect(Collectors.toUnmodifiableList());
        if (list.size() < count) {
            count = list.size();
        }
        return list.subList(0, count);
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
