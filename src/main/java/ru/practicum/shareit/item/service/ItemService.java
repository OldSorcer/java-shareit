package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Item item, Long ownerId);
    Item updateItem(Long itemId, Item item, Long ownerId);
    void deleteItemById(Long itemId, Long ownerId);
    List<Item> getAllItems();
    List<Item> getItemByOwnerId(Long ownerId);
    Item getItemById(Long itemId);
    List<Item> searchBy(String word);
}