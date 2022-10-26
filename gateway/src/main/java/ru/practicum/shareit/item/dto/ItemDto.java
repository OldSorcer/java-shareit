package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.groups.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotNull(groups = {Create.class}, message = "Название вещи не может быт пустым")
    @NotBlank(groups = {Create.class}, message = "Название вещи не может быт пустым")
    private String name;
    @NotNull(groups = {Create.class}, message = "Описание вещи не может быт пустым")
    @NotBlank(groups = {Create.class}, message = "Описание вещи не может быт пустым")
    private String description;
    @NotNull(groups = {Create.class}, message = "Необходимо установить доступ к предмету")
    private Boolean available;
    private Long ownerId;
    private Long requestId;
}