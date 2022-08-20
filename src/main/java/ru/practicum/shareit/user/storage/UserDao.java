package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    User createUser(User user);

    User updateUser(Long userId, User user);

    Optional<User> deleteUserById(Long userId);

    Optional<User> findByUserId(Long userId);

    List<User> getAllUsers();
}
