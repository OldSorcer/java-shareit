package ru.practicum.shareit.user.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    private final User user = new User(1L, "User", "user@email.ru");
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.save(user);
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void shouldFindUserByEmail() {
        Optional<User> foundedUser = userRepository.findByEmailContainsIgnoreCase("user@email.ru");
        assertTrue(foundedUser.isPresent());
        assertEquals(user.getEmail(), foundedUser.get().getEmail());
        assertEquals(user.getName(), foundedUser.get().getName());
    }

    @Test
    void shouldFindUserByEmailIgnoreCase() {
        Optional<User> foundedUser = userRepository.findByEmailContainsIgnoreCase("uSeR@EmAiL.ru");
        assertTrue(foundedUser.isPresent());
        assertEquals(user.getEmail(), foundedUser.get().getEmail());
    }
}