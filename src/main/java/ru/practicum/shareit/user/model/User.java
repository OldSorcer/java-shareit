package ru.practicum.shareit.user.model;

import lombok.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.*;

/**
 * // TODO .
 */
@NoArgsConstructor
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    public User(UserDto userDto) {
        this.id = userDto.getId();
        this.name = userDto.getName();
        this.email = userDto.getEmail();
    }
}