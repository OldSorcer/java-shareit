package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public final class ItemDtoMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item);
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(itemDto);
    }
}
