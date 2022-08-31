package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validator.groups.Create;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    private String name;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class})
    private String email;

    public User(UserDto userDto) {
        this.id = userDto.getId();
        this.name = userDto.getName();
        this.email = userDto.getEmail();
    }
}
