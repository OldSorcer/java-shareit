package ru.practicum.shareit.item.storage;

import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    Item createItem(Item item, Long ownerId);
    Item updateItem(Long itemId, Item item, Long ownerId);
    Optional<Item> deleteItemById(Long itemId);
    List<Item> getItemsByOwnerId(Long ownerId);
    Optional<Item> getItemById(Long itemId);
    List<Item> getAllItems();
    List<Item> searchBy(String word);
}
