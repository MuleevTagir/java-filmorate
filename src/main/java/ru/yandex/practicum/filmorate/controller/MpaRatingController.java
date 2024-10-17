package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaRatingController {

    private final MpaRatingService mpaRatingService;

    public MpaRatingController(MpaRatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MpaRating getMpaById(@PathVariable int id) throws NotFoundException {
        return mpaRatingService.getMpaById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MpaRating> getAllMpa(@RequestParam(defaultValue = "5") int limit) {
        return mpaRatingService.getAllMpa(limit);
    }
}
