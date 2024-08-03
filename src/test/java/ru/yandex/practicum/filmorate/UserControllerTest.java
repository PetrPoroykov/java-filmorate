package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    private UserController userController = new UserController( inMemoryUserStorage);



    @DisplayName("Должнен быть добавлен пользователь")
    @Test
    void userShouldBeAdded() {
        User user = new User();
        user.setEmail("www@ttt");
        user.setLogin("qwerty");
        user.setName("Ivan");
        user.setBirthday((LocalDate.of(1995, 12, 28)));
        int countUser = inMemoryUserStorage.findAll().size();
        userController.create(user);
        int countUserNew = inMemoryUserStorage.findAll().size();
        assertEquals(countUser + 1, countUserNew, "Неверное количество фильмов");
    }

    @DisplayName("Должно выбросываться исключение ValidationException когда дата рождения указана в будующем")
    @Test
    public void shouldBeThrownValidationExceptionWhenDateBirthInFuture() {
        ValidationException exc = Assertions.assertThrows(ValidationException.class, () -> {
            User user = new User();
            user.setEmail("www@ttt");
            user.setLogin("qwerty");
            user.setName("Ivan");
            user.setBirthday((LocalDate.of(2024, 12, 30)));
            userController.create(user);
        });
        assertEquals("Дата рождения не может быть в будущем", exc.getMessage());
    }

    @DisplayName("Должно выбросываться исключение ValidationException " +
            "когда нет Email.")
    @Test
    public void shouldBeThrownValidationExceptionEmfilIsEmpty() {
        ValidationException exc = Assertions.assertThrows(ValidationException.class, () -> {
            User user = new User();
            user.setEmail("");
            user.setLogin("qwerty");
            user.setName("Ivan");
            user.setBirthday((LocalDate.of(2024, 1, 30)));
            userController.create(user);
        });
        assertEquals("Email не может быть пустым и должен содержать символ @", exc.getMessage());
    }

    @DisplayName("Должно выбросываться исключение ValidationException " +
            "когда в  Email нет знак @.")
    @Test
    public void shouldBeThrownValidationExceptionWhenEmailDoesNotContainAt() {
        ValidationException exc = Assertions.assertThrows(ValidationException.class, () -> {
            User user = new User();
            user.setEmail("wwwttt");
            user.setLogin("qwerty");
            user.setName("Ivan");
            user.setBirthday((LocalDate.of(2024, 1, 30)));
            userController.create(user);
        });
        assertEquals("Email не может быть пустым и должен содержать символ @", exc.getMessage());
    }

    @DisplayName("Должно выбросываться исключение ValidationException " +
            "когда Login пустой.")
    @Test
    public void shouldBeThrownValidationExceptionWhenLoginIsEmpty() {
        ValidationException exc = Assertions.assertThrows(ValidationException.class, () -> {
            User user = new User();
            user.setEmail("w@e");
            user.setLogin("");
            user.setName("Ivan");
            user.setBirthday((LocalDate.of(2024, 1, 30)));
            userController.create(user);
        });
        assertEquals("Login не может быть пустым и  не должен  содержать пробел", exc.getMessage());
    }

    @DisplayName("Должно выбросываться исключение ValidationException " +
            "когда Login содержит пробел.")
    @Test
    public void shouldBeThrownValidationExceptionWhenLoginСontainsSpace() {
        ValidationException exc = Assertions.assertThrows(ValidationException.class, () -> {
            User user = new User();
            user.setEmail("w@e");
            user.setLogin("rrr iii");
            user.setName("Ivan");
            user.setBirthday((LocalDate.of(2024, 1, 30)));
            userController.create(user);
        });
        assertEquals("Login не может быть пустым и  не должен  содержать пробел", exc.getMessage());
    }

    @DisplayName("Имя должно использовать логин если имя пустое")
    @Test
    public void shouldBeNameIsEquivalentToLoginWhenNameIsEmpty() {
        User user = new User();
        user.setEmail("w@e");
        user.setLogin("rrriii");
        user.setName("");
        user.setBirthday((LocalDate.of(2024, 1, 30)));
        userController.create(user);
        assertEquals("rrriii", user.getName(), "Логин не используется вместо пустого имени");
    }

    @DisplayName("Пользовтель должнен быть оновлен")
    @Test
    void filmShouldBeUpdata() {

        User user = new User();
        user.setEmail("www@ttt");
        user.setLogin("qwe@rty");
        user.setName("Ivan");
        user.setBirthday((LocalDate.of(1995, 12, 28)));
        int countUser = userController.findAll().size();
        userController.create(user);
        int countUserNew = userController.findAll().size();
        assertEquals(countUser + 1, countUserNew, "Неверное количество фильмов");
        ;
        User user1 = new User();
        user1.setId(user.getId());
        user1.setEmail("www@ttt");
        user1.setLogin("Обновленныйлогин");
        user1.setName("Ivan");
        user1.setBirthday((LocalDate.of(1995, 12, 28)));
        userController.update(user1);
        assertEquals("Обновленныйлогин", user.getLogin(), "Данные не обновились");
        assertEquals(countUser + 1, countUserNew, "Неверное количество пользователей после обновления");
    }
}

