package ru.practicum.shareit.user.model;

import lombok.*;
import ru.practicum.shareit.user.dto.UserDto;

@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;

    public User(UserDto userDto) {
        this.id = userDto.getId();
        this.name = userDto.getName();
        this.email = userDto.getEmail();
    }
}