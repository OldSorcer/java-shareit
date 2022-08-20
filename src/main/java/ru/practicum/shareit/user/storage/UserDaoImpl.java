package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class UserDaoImpl implements UserDao {
    private Map<Long, User> users;
    private long idCounter;

    public UserDaoImpl() {
        this.users = new HashMap<>();
        this.idCounter = 0L;
    }

    @Override
    public User createUser(User user) {
        checkDuplicateEmail(user.getEmail());
        user.setId(++idCounter);
        users.put(idCounter, user);
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        if (Objects.nonNull(user.getEmail())) {
            checkDuplicateEmail(user.getEmail());
        }
        User userForUpdate = setUserStatement(userId, user);
        users.put(userId, userForUpdate);
        return userForUpdate;
    }

    @Override
    public Optional<User> deleteUserById(Long userId) {
        return Optional.ofNullable(users.remove(userId));
    }

    @Override
    public Optional<User> findByUserId(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    private void checkDuplicateEmail(String email) {
        boolean isDuplicated = users.values().stream()
                .anyMatch(u -> u.getEmail().equals(email));
        if (isDuplicated) {
            throw new InvalidArgumentException(String.format("Пользователь с e-mail %s уже существует", email));
        }
    }

    private User setUserStatement(Long userId, User user) {
        User foundedUser = findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с ID %d не существует",
                        userId)));
        if (Objects.nonNull(user.getName()) && !user.getName().isBlank()) {
            foundedUser.setName(user.getName());
        }
        if (Objects.nonNull(user.getEmail()) && !user.getEmail().isBlank()) {
            foundedUser.setEmail(user.getEmail());
        }
        return foundedUser;
    }
}