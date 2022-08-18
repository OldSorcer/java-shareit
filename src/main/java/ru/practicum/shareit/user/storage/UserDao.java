package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    User createUser(User user);
    User updateUser(Long userId, User user);
    void deleteUserById(Long userId);
    User findByUserId(Long userId);
    List<User> getAllUsers();
}
