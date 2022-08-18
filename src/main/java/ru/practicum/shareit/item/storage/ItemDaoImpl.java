package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemDaoImpl implements ItemDao {
    private Map<Long, Item> items;
    private Long idCounter;

    public ItemDaoImpl() {
        this.items = new HashMap<>();
        this.idCounter = 0L;
    }
    @Override
    public Item createItem(Item item) {
        item.setId(++idCounter);
        items.put(idCounter, item);
        return item;
    }

    @Override
    public Item updateItem(Long itemId, Item item) {
        Item updatedItem = setItemStatement(itemId, item);
        items.put(itemId, updatedItem);
        return updatedItem;
    }

    @Override
    public Optional<Item> deleteItemById(Long itemId) {
         return Optional.ofNullable(items.remove(itemId));
    }

    @Override
    public List<Item> getItemsByOwnerId(Long ownerId) {
        return items.values().stream()
                .filter(i -> i.getOwner().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> getItemById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getAllItems() {
        return List.copyOf(items.values());
    }

    private Item setItemStatement(Long itemId, Item item) {
        Item foundedItem = getItemById(itemId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Предмета с ID %d не существует", itemId)));
        if (Objects.nonNull(item.getName()) && !item.getName().isBlank()) {
            foundedItem.setName(item.getName());
        }
        if (Objects.nonNull(item.getDescription()) && !item.getDescription().isBlank()) {
            foundedItem.setDescription(item.getDescription());
        }
        if (Objects.nonNull(item.getAvailable())) {
            foundedItem.setAvailable(item.getAvailable());
        }
        return foundedItem;
    }
}