package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserService {

    private int id = 0;
    private final Map<Integer, User> userMap = new HashMap<>();

    public User create(User user) {
        user.setId(getNextId());
        userMap.put(user.getId(), user);
        log.info("Добавление пользователя: {}.", user);
        return user;
    }

    public User update(User user) {
        if (!this.userMap.containsKey(user.getId())) {
            String errorMsg = String.format("Пользователь с id=%d не найден.", user.getId());
            log.error(errorMsg);
            throw new NotFoundException(errorMsg);
        }

        this.userMap.put(user.getId(), user);
        log.info("Обновление пользователя: {}.", user);
        return user;
    }

    public List<User> getAll() {
        log.info("Список всех пользователей.");
        return new ArrayList<>(userMap.values());
    }

    private int getNextId() {
        return ++id;
    }
}
