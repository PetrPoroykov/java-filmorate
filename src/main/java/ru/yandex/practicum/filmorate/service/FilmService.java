package ru.yandex.practicum.filmorate.service;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Component
@Data
public class FilmService {



    public void addLike(Film film, User user) {
        film.getLikes().add(user.getId());
    }

    public void deleteLike(Film film, User user) {
        film.getLikes().remove(user.getId());
    }


}
