package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FilmService {

    private int id = 0;

    private final Map<Integer, Film> filmMap = new HashMap<>();

    public Film add(Film film) {
        film.setId(getNextId());
        filmMap.put(film.getId(), film);

        log.info("Добавление фильма: {}.", film);
        return film;
    }


    public Film update(Film film) {
        if (!this.filmMap.containsKey(film.getId())) {
            String errorMsg = String.format("Фильм с id=%d не найден.", film.getId());
            log.error(errorMsg);
            throw new NotFoundException(errorMsg);
        }

        this.filmMap.put(film.getId(), film);
        log.info("Обновление фильма: {}.", film);
        return film;
    }

    public List<Film> getAll() {
        log.info("Список фильмов.");
        return new ArrayList<>(filmMap.values());
    }

    private int getNextId() {
        return ++id;
    }
}
