package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info(
                "[x] ShateIt-server: Получен POST запрос к эндпоинту /users"
        );
        User createdUser = userService.createUser(UserDtoMapper.toUser(userDto));
        return UserDtoMapper.toUserDto(createdUser);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info(
                "[x] ShateIt-server: Получен PATCH запрос к эндпоинту /users/{}", userId
        );
        User updatedUser = userService.updateUser(userId, UserDtoMapper.toUser(userDto));
        return UserDtoMapper.toUserDto(updatedUser);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info(
                "[x] ShateIt-server: Получен GET запрос к эндпоинту /users/{}", userId
        );
        return UserDtoMapper.toUserDto(userService.getUserById(userId));
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info(
                "[x] ShateIt-server: Получен GET запрос к эндпоинту /users"
        );
        return userService.getAllUsers().stream()
                .map(UserDtoMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.info(
                "[x] ShateIt-server: Получен DELETE запрос к эндпоинту /users/{}", userId
        );
        userService.deleteUserById(userId);
    }
}