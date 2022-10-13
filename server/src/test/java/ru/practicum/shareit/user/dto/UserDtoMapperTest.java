package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoMapperTest {
    private static User user;
    private static UserDto userDto;

    @BeforeAll
    static void beforeAll() {
        user = new User(1L, "User", "user@email.ru");
        userDto = new UserDto(1L, "User", "user@email.ru");
    }

    @Test
    void shouldReturnUserDto() {
        UserDto result = UserDtoMapper.toUserDto(user);
        assertEquals(userDto, result);
    }

    @Test
    void shouldReturnUser() {
        User result = UserDtoMapper.toUser(userDto);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
    }
}