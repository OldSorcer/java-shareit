package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    @Override
    public Item createItem(Item item, Long ownerId) {
        User foundedUser = checkUser(ownerId);
        item.setOwner(foundedUser);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Long itemId, Item item, Long ownerId) {
        checkUser(ownerId);
        Item updatedItem = setItemStatement(itemId, item, ownerId);
        return itemRepository.save(updatedItem);
    }

    @Override
    public void deleteItemById(Long itemId, Long ownerId) {
        checkUser(ownerId);
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> getItemByOwnerId(Long ownerId) {
        checkUser(ownerId);
        return itemRepository.findByOwnerIdOrderById(ownerId);
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Вещи с ID %d не существует", itemId)));
    }

    @Override
    public List<Item> searchBy(String word) {
        if (word.isEmpty() || word.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchBy(word);
    }

    private User checkUser(Long userId) {
        Optional<User> foundedUser;
        if (Objects.nonNull(userId)) {
            foundedUser = userRepository.findById(userId);
        } else {
            throw new InvalidArgumentException("Идентификатор владельца вещи не установлен");
        }
        if (foundedUser.isEmpty()) {
            throw new EntityNotFoundException(String.format("Пользователя с ID %d не существует", userId));
        }
        return foundedUser.get();
    }

    private Item setItemStatement(Long itemId, Item item, Long ownerId) {
        Item foundedItem = getItemById(itemId);
        if (!foundedItem.getOwner().getId().equals(ownerId)) {
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