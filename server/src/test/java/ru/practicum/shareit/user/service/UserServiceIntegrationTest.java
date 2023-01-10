package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceIntegrationTest {
    private final User user = new User(null, "User", "user@email.com");
    private final User updatedUser = new User(1L, "Updated", "updated@email.com");
    @Autowired
    private UserService userService;

    @Test
    void updateUser() {
        User createdUser = userService.createUser(user);
        User result = userService.updateUser(createdUser.getId(), updatedUser);
        assertEquals(createdUser.getId(), result.getId());
        assertEquals(updatedUser.getName(), result.getName());
        assertEquals(updatedUser.getEmail(), result.getEmail());
    }
}