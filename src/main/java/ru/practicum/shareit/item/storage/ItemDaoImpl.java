//package ru.practicum.shareit.item.storage;
//
//import org.springframework.stereotype.Repository;
//import ru.practicum.shareit.exception.EntityNotFoundException;
//import ru.practicum.shareit.item.model.Item;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Repository
//public class ItemDaoImpl implements ItemDao {
//    private final Map<Long, Item> items;
//    private Long idCounter;
//
//    public ItemDaoImpl() {
//        this.items = new HashMap<>();
//        this.idCounter = 0L;
//    }
//
//    @Override
//    public Item createItem(Item item, Long ownerId) {
//        item.setId(++idCounter);
//        item.setOwner(ownerId);
//        items.put(idCounter, item);
//        return item;
//    }
//
//    @Override
//    public Item updateItem(Long itemId, Item item, Long ownerId) {
//        Item updatedItem = setItemStatement(itemId, item, ownerId);
//        items.put(itemId, updatedItem);
//        return updatedItem;
//    }
//
//    @Override
//    public Optional<Item> deleteItemById(Long itemId) {
//        return Optional.ofNullable(items.remove(itemId));
//    }
//
//    @Override
//    public List<Item> getItemsByOwnerId(Long ownerId) {
//        return items.values().stream()
//                .filter(i -> i.getOwner().equals(ownerId))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Optional<Item> getItemById(Long itemId) {
//        return Optional.ofNullable(items.get(itemId));
//    }
//
//    @Override
//    public List<Item> getAllItems() {
//        return List.copyOf(items.values());
//    }
//
//    @Override
//    public List<Item> searchBy(String word) {
//        return items.values().stream()
//                .filter(i -> isContainText(i.getName(), word)
//                        || isContainText(i.getDescription(), word))
//                .filter(Item::getAvailable)
//                .collect(Collectors.toList());
//    }
//
//    private boolean isContainText(String text, String word) {
//        return text.toLowerCase().contains(word);
//    }
//
//}