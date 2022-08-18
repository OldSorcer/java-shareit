package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserDao;

import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public User createUser(User user) {
        return userDao.createUser(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userDao.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Пользователь с ID %d не найден", userId)));
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void deleteUserById(Long userId) {
        userDao.deleteUserById(userId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Пользователь с ID %d не найден", userId)));
    }

    @Override
    public User updateUser(Long userId, User user) {
        return userDao.updateUser(userId, user);
    }
}