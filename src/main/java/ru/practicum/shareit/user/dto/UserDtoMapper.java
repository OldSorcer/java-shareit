package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public final class UserDtoMapper {
    public static User toUser(UserDto userDto) {
        return new User(userDto);
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user);
    }
}
