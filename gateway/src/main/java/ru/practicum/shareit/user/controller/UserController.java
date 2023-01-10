package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.groups.Create;
import ru.practicum.shareit.user.dto.UserDto;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated({Create.class}) UserDto userDto) {
        log.info(
                "[x] ShateIt-gateway: Получен POST запрос к эндпоинту /users"
        );
        return userClient.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info(
                "[x] ShateIt-gateway: Получен PATCH запрос к эндпоинту /users/{}", userId
        );
        return userClient.update(userId, userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info(
                "[x] ShateIt-gateway: Получен GET запрос к эндпоинту /users/{}", userId
        );
        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info(
                "[x] ShateIt-gateway: Получен GET запрос к эндпоинту /users"
        );
        return userClient.getAll();
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.info(
                "[x] ShateIt-gateway: Получен DELETE запрос к эндпоинту /users/{}", userId
        );
        userClient.deleteById(userId);
    }
}