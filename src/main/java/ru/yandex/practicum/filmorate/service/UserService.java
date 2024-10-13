package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private int id = 0;
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        log.info("Добавление пользователя: {}.", user);
        user.setId(getNextId());
        return userStorage.add(user);
    }

    public User update(User user) {
        log.info("Обновление пользователя: {}.", user);
        userStorage.update(user);
        return user;
    }

    public boolean areFriends(int userId, int friendId) {
        User user = userStorage.getById(userId);
        return user.getFriends().contains(friendId);
    }

    public User getUserById(int id) {
        return userStorage.getById(id);
    }

    public List<User> getAll() {
        log.info("Список всех пользователей.");
        return userStorage.getAll();
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (this.areFriends(user.getId(), friendId)) {
            throw new ValidationException("Пользователь уже добавлен в друзья");
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.update(user);
        userStorage.update(friend);
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        if (!this.areFriends(user.getId(), friendId)) {
            return;
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.update(user);
        userStorage.update(friend);
    }

    public List<User> getFriends(int userId) {
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        User user = userStorage.getById(userId);
        User otherUser = userStorage.getById(otherId);

        Set<Integer> commonFriendsIds = new HashSet<>(user.getFriends());
        commonFriendsIds.retainAll(otherUser.getFriends());
        List<User> commonFriends = new ArrayList<>();
        for (int friendId : commonFriendsIds) {
            commonFriends.add(userStorage.getById(friendId));
        }
        return commonFriends;
    }

    private int getNextId() {
        return ++id;
    }
}
