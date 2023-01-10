package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с ID %d не существует",
                        userId)));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User updateUser(Long userId, User user) {
        User updatedUser = setStatement(userId, user);
        return userRepository.save(updatedUser);
    }

    private User setStatement(Long userId, User user) {
        User updatedUser = getUserById(userId);
        if (Objects.nonNull(user.getName())) {
            updatedUser.setName(user.getName());
        }
        if (Objects.nonNull(user.getEmail())) {
            updatedUser.setEmail(user.getEmail());
        }
        updatedUser.setId(userId);
        return updatedUser;
    }
}