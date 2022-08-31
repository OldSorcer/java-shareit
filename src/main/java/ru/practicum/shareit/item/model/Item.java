package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validator.groups.Create;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    private String name;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private Long owner;
    @OneToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    public Item(ItemDto itemDto) {
        this.id = itemDto.getId();
        this.name = itemDto.getName();
        this.description = itemDto.getDescription();
        this.available = itemDto.getAvailable();
        this.owner = itemDto.getOwner();
        this.request = itemDto.getRequest();
    }
}
