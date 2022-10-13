package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private final User user = new User(1L, "User", "user@email.ru");

    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfUserNotFound() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Exception exc = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
        assertEquals("Пользователя с ID 1 не существует", exc.getMessage());
    }

    @Test
    void shouldReturnUserEntity() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        assertEquals(user, userService.getUserById(1L));
    }
}