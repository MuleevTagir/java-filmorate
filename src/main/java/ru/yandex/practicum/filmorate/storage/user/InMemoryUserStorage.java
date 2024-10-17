package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public abstract class InMemoryUserStorage implements UserStorage {

    private final List<User> users = new ArrayList<>();

    @Override
    public Optional<User> getUserById(int id) {
        return users.stream().filter(user -> user.getId() == id).findFirst();
    }

    @Override
    public User update(User user) throws NotFoundException {
        Optional<User> existingUser = getUserById(user.getId());
        if (existingUser.isPresent()) {
            int index = users.indexOf(existingUser.get());
            users.set(index, user);
            return user;
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public User add(User user) {
        users.add(user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    @Override
    public List<User> getFriends(int userId) throws NotFoundException {
        User user = getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        return user.getFriends().stream()
                .map(friendId -> {
                    try {
                        return getUserById(friendId)
                                .orElseThrow(() -> new NotFoundException("Friend with id " + friendId + " not found"));
                    } catch (NotFoundException e) {
                        return null;
                    }
                })
                .filter(f -> !Objects.isNull(f))
                .collect(Collectors.toList());
    }
}