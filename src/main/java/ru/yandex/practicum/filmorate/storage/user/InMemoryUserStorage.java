package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Data
public class InMemoryUserStorage implements UserStorage {



    private final Map<Long, User> users = new HashMap<>();



    @Override
    public void create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
    }

    @Override
    public User update(User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

//            ValidateService.validateUser(newUser);

            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            return oldUser;
        }
        throw new NotFoundException("Фильм с id = " + newUser.getId() + " не найден");
    }

    @Override
    public User delete(User film) {
        if (film.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsValue(film)) {
            users.remove(film);
            return film;
        }
        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
    }

    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User getById(Long id) {
        return users.values().stream().filter(user -> user.getId() == id)
                .findFirst().orElseThrow(() -> new NotFoundException("Пользователь с номером " + id + " не найден"));
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

