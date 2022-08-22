package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemDao;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserDao;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemDao itemDao;
    private final UserDao userDao;

    @Override
    public Item createItem(Item item, Long ownerId) {
        checkUser(ownerId);
        return itemDao.createItem(item, ownerId);
    }

    @Override
    public Item updateItem(Long itemId, Item item, Long ownerId) {
        checkUser(ownerId);
        return itemDao.updateItem(itemId, item, ownerId);
    }

    @Override
    public void deleteItemById(Long itemId, Long ownerId) {
        checkUser(ownerId);
        itemDao.deleteItemById(itemId);
    }

    @Override
    public List<Item> getAllItems() {
        return itemDao.getAllItems();
    }

    @Override
    public List<ItemDto> getItemByOwnerId(Long ownerId) {
        checkUser(ownerId);
        return itemDao.getItemsByOwnerId(ownerId).stream()
                .map(ItemDtoMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemDao.getItemById(itemId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Вещи с ID %d не существует", itemId)));
    }

    @Override
    public List<ItemDto> searchBy(String word) {
        if (word.isEmpty() || word.isBlank()) {
            return new ArrayList<>();
        }
        return itemDao.searchBy(word.toLowerCase()).stream()
                .map(ItemDtoMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void checkUser(Long userId) {
        Optional<User> foundedUser;
        if (Objects.nonNull(userId)) {
            foundedUser = userDao.findByUserId(userId);
        } else {
            throw new InvalidArgumentException("Идентификатор владельца вещи не установлен");
        }
        if (foundedUser.isEmpty()) {
            throw new EntityNotFoundException(String.format("Пользователя с ID %d не существует", userId));
        }
    }
}