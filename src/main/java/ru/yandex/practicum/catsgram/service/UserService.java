package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User create(@RequestBody final User user) {

        if(user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ConditionsNotMetException("Email is required");
        }

        users.values().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findFirst()
                .ifPresent(u -> {
                    throw new DuplicatedDataException("Email is already in use");
                });

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());

        users.put(user.getId(), user);
        return user;
    }

    public User update(@RequestBody User newUser) {
        // Проверка ID
        if(newUser.getId() == null) {
            throw new ConditionsNotMetException("Id is required");
        }

        User oldUser = users.get(newUser.getId());
        if(oldUser == null) {
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }

        // Проверка нового email на дубликат
        if (newUser.getEmail() != null && !newUser.getEmail().equals(oldUser.getEmail())) {
            users.values().stream()
                    .filter(u -> u.getEmail().equals(newUser.getEmail()))
                    .findFirst()
                    .ifPresent(u -> {
                        throw new DuplicatedDataException("Email is already in use");
                    });
        }

        // Обновляем только не-null поля
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getUsername() != null) {
            oldUser.setUsername(newUser.getUsername());
        }
        if (newUser.getPassword() != null) {
            oldUser.setPassword(newUser.getPassword());
        }
        return oldUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public Optional<User> findUserById(String userId) {
        return users.values().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }
}
