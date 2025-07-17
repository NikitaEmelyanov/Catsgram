package ru.yandex.practicum.catsgram.model;

import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@Getter
@EqualsAndHashCode(of = { "email" })
public class User {
    private long id;
    private String username;
    private String email;
    private String password;
    private Instant registrationDate;
}
