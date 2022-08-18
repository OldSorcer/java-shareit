package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
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
        User userForUpdate = setUserStatement(userId, user);
        users.put(userId, userForUpdate);
        return userForUpdate;
    }

    @Override
    public void deleteUserById(Long userId) {
        User deletedUser = users.remove(userId);
        if (Objects.isNull(deletedUser)) {
            throw new NoSuchElementException(String.format("Пользователя с ID %d не существует", userId));
        }
    }

    @Override
    public User findByUserId(Long userId) {
        return users.values().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("Пользователь с ID %d не найден", userId)));
    }

    @Override
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    private void checkDuplicateEmail(String email) {
        boolean isDuplicated = users.values().stream().noneMatch(u -> u.getEmail().equals(email));
        if (isDuplicated) {
            throw new IllegalArgumentException(String.format("Пользователь с e-mail %s уже существует", email));
        }
    }

    private User setUserStatement(Long userId, User user) {
        User foundedUser = findByUserId(userId);
        if (Objects.nonNull(user.getName()) && !user.getName().isBlank()) {
            foundedUser.setName(user.getName());
        }
        if (Objects.nonNull(user.getEmail()) && !user.getEmail().isBlank()) {
            foundedUser.setEmail(user.getEmail());
        }
        return foundedUser;
    }
}