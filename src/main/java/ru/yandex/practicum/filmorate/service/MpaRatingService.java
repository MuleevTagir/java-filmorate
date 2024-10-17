package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingStorage;

import java.util.List;

@Service
public class MpaRatingService {
    private final MpaRatingStorage mpaRatingStorage;

    public MpaRatingService(@Qualifier("MpaRatingDbStorage") MpaRatingStorage mpaRatingStorage) {
        this.mpaRatingStorage = mpaRatingStorage;
    }

    public List<MpaRating> getAllMpa(int limit) {
        return mpaRatingStorage.getAllMpa(limit);
    }

    public MpaRating getMpaById(int id) throws NotFoundException {
        return mpaRatingStorage.getMpaById(id)
                .orElseThrow(() -> new NotFoundException("MPA рейтинг с id " + id + " не найден"));
    }
}
