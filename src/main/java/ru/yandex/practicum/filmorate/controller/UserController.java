package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {

        if (user.getEmail() == null || user.getEmail().isBlank() || user.getEmail().indexOf('@') == -1) {
            throw new ConditionsNotMetException("Email не может быть пустым и должен содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || !(user.getLogin().indexOf(' ') == -1)) {
            throw new ConditionsNotMetException("Login не может быть пустым и  не должен  содержать пробел");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new DateIsNotValidException("Дата рождения не может быть в будущем");
        }

        user.setId(getNextId());

        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь: " + user.getName() + ", пользователю  присвоен ID: " + user.getId());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {

        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());

            if (newUser.getEmail() == null || newUser.getEmail().isBlank() || newUser.getEmail().indexOf('@') == -1) {
                throw new ConditionsNotMetException("Email не может быть пустым и должен содержать символ @");
            }
            if (newUser.getLogin() == null || newUser.getLogin().isBlank() || !(newUser.getLogin().indexOf(' ') == -1)) {
                throw new ConditionsNotMetException("Login не может быть пустым и  не должен  содержать пробел");
            }
            if (newUser.getName() == null || newUser.getName().isBlank()) {
                newUser.setName(newUser.getLogin());
            }
            if (newUser.getBirthday().isAfter(LocalDate.now())) {
                throw new DateIsNotValidException("Дата рождения не может быть в будущем");
            }

            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setName(newUser.getName());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Данные пользователя с ID " + oldUser.getId() + " обновлены");
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
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

