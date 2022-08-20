package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EntityNotFoundException;
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
    public Item createItem(Item item, Long ownerId) {
        item.setId(++idCounter);
        item.setOwner(ownerId);
        items.put(idCounter, item);
        return item;
    }

    @Override
    public Item updateItem(Long itemId, Item item, Long ownerId) {
        Item updatedItem = setItemStatement(itemId, item, ownerId);
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

    @Override
    public List<Item> searchBy(String word) {
        return items.values().stream()
                .filter(i -> i.getName().toLowerCase().contains(word)
                        || i.getDescription().toLowerCase().contains(word)).filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    private Item setItemStatement(Long itemId, Item item, Long ownerId) {
        Item foundedItem = getItemById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Предмета с ID %d не существует", itemId)));
        if (!foundedItem.getOwner().equals(ownerId)) {
            throw new EntityNotFoundException(String.format("Вещь с ID %d и ID владельца %d не найдена",
                    itemId, ownerId));
        }
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