package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.ErrorMessage;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(NotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(BadRequest.class)
    public ResponseEntity<ErrorMessage> badRequestException(BadRequest exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
