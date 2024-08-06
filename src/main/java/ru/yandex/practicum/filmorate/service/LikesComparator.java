package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

@Component
public class LikesComparator implements Comparator<Film> {
    @Override
    public int compare(Film p1, Film p2) {
        return p2.getLikes().size() - p1.getLikes().size();
    }
}
