package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class FilmService {



    public void addLike(Film film, User user) {
        film.getLikes().add(user.getId());
    }

    public void deleteLike(Film film, User user) {
        film.getLikes().remove(user.getId());
    }


}
