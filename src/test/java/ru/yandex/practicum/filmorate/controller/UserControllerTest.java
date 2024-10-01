package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

public class UserControllerTest {

    private UserService userService;

    private User user;

    @BeforeEach
    void init() {
        this.userService = new UserService();
        this.user = new User();

        this.user.setLogin("Test");
        this.user.setEmail("test@yandex.ru");
        this.user.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    void validateUserWithCorrectDate() {
        this.userService.create(user);
        Assertions.assertEquals(1, this.userService.getAll().size(), "разрмер getAll должно быть 1");
    }

    @Test
    void notFoundExceptionTest() {
        userService.create(user);
        user.setId(2);

        Assertions.assertThrows(NotFoundException.class, () -> {
            userService.update(user);
        }, "Должно быть выброшено исключения из-за отсутсвия User с id 2");
    }
}
