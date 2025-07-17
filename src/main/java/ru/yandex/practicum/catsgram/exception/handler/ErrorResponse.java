package ru.yandex.practicum.catsgram.exception.handler;

import lombok.Data;

@Data
public class ErrorResponse {
    private final String error;
}