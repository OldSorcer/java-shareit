package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    Item createItem(Item item);
    Item updateItem(Item item);
    void deleteItemById(Long itemId);
    List<Item> getItemsByOwnerId(Long ownerId);
    Item getItemById(Long itemId);
}
