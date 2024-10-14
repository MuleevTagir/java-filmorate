package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User add(User user);

    User update(User user) throws NotFoundException;

    Optional<User> getUserById(int id);

    List<User> getAll();

    List<User> getFriends(int userId) throws NotFoundException;
}
