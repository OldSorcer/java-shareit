package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Item item);
    Item updateItem(Item item);
    void deleteItemById(Long itemId);
    List<Item> getAllItems();
    Item getItemByOwnerId(Long ownerId);
}
