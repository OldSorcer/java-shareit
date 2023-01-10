package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.groups.Create;
import ru.practicum.shareit.groups.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NotNull(groups = {Create.class}, message = "Имя пользователя не может быть пустым")
    private String name;
    @NotNull(groups = {Create.class}, message = "E-mail пользователя не может быть пустым")
    @Email(groups = {Create.class, Update.class}, message = "Введен некорректный e-mail адрес")
    private String email;
}
