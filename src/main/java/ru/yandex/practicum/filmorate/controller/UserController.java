package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    final UserStorage userStorage;
    final UserService userService;

    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @PostMapping
    public User create(@RequestBody User user) {

        ValidateService.validateUser(user);

        userStorage.create(user);
        log.info("Добавлен новый пользователь: " + user.getName() + ", пользователю  присвоен ID: " + user.getId());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        ValidateService.validateUser(newUser);
        return userStorage.update(newUser);
    }

    @GetMapping("/{id}")
    public User userById(@PathVariable final Long id) {
        System.out.println(id);
        return userStorage.getById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable final Long id, @PathVariable final Long friendId) {
        userService.addFriend(userStorage.getById(id), userStorage.getById(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable final Long id, @PathVariable final Long friendId) {
        userService.deleteFriend(userStorage.getById(id), userStorage.getById(friendId));
    }

    @GetMapping("/{id}/friends")
    public Collection<User> friendsById(@PathVariable final Long id) {
        Set<User> friends = new HashSet<>();
        for (Long friendId : userStorage.getById(id).getFriends()) {
            User user = userStorage.getById(friendId);
            friends.add(user);
        }
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> jointFriends(@PathVariable final Long id, @PathVariable final Long otherId) {
        Set<User> friends = new HashSet<>();
        Set<User> otherFriends = new HashSet<>();
        Set<User> common = new HashSet<>();
        for (Long friendId : userStorage.getById(id).getFriends()) {
            User user = userStorage.getById(friendId);
            friends.add(user);

        }
        for (Long other : userStorage.getById(otherId).getFriends()) {
            User user = userStorage.getById(other);
            otherFriends.add(user);
        }
        friends.retainAll(otherFriends);
        return friends;
    }
}

