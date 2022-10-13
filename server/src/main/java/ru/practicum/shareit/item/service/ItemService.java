package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Item item, Long ownerId);

    Item updateItem(Long itemId, Item item, Long ownerId);

    void deleteItemById(Long itemId, Long ownerId);

    List<Item> getAllItems();

    List<ItemInfoDto> getItemByOwnerId(Long ownerId, int from, int size);

    ItemInfoDto getItemById(Long itemId, Long userId);

    List<Item> searchBy(String word, int from, int size);

    Comment createComment(Comment comment, Long userId, Long itemId);
}