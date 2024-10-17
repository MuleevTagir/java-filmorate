package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private int id = 0;
    private final UserStorage userStorage;

    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        log.info("Добавление пользователя: {}.", user);
        user.setId(getNextId());
        return userStorage.add(user);
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) throws NotFoundException {
        log.info("Обновление пользователя: {}.", user);
        Optional<User> existingUser = userStorage.getUserById(user.getId());
        if (existingUser.isPresent()) {
            userStorage.update(user);
            return user;
        } else {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
    }

    public boolean existsById(int userId) {
        return userStorage.getUserById(userId).isPresent();
    }

    public boolean areFriends(int userId, int friendId) {
        return userStorage.areFriends(userId, friendId);
    }

    public Optional<User> getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public List<User> getAll() {
        log.info("Список всех пользователей.");
        return userStorage.getAll();
    }

    public void addFriend(int userId, int friendId) throws NotFoundException {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        User friend = userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Друг с id " + friendId + " не найден"));

        if (userStorage.areFriends(userId, friendId)) {
            throw new ValidationException("Пользователь уже добавлен в друзья");
        }

        userStorage.addFriendship(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) throws NotFoundException {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        User friend = userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Друг с id " + friendId + " не найден"));

        userStorage.removeFriendship(userId, friendId);
    }

    public List<User> getFriends(int userId) throws NotFoundException {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) throws NotFoundException {
        User user = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        User otherUser = userStorage.getUserById(otherId)
                .orElseThrow(() -> new NotFoundException(
                        "Другой пользователь не найден"));

        Set<Integer> commonFriendsIds = new HashSet<>(user.getFriends());
        commonFriendsIds.retainAll(otherUser.getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (int friendId : commonFriendsIds) {
            userStorage.getUserById(friendId).ifPresent(commonFriends::add);
        }
        return commonFriends;
    }

    private int getNextId() {
        return ++id;
    }
}
