package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.EntityCreateException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

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
    public List<ItemInfoDto> getItemByOwnerId(Long ownerId) {
        checkUser(ownerId);
        List<Item> items = itemRepository.findByOwnerIdOrderById(ownerId);
        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking;
        Booking nextBooking;
        List<ItemInfoDto> itemInfoDtos = new ArrayList<>();
        List<Comment> comments;
        for (Item item : items) {
            lastBooking = bookingRepository.findFirstByItem_IdAndEndBeforeOrderByEndDesc(item.getId(), now);
            nextBooking = bookingRepository.findFirstByItem_IdAndStartAfterOrderByStartAsc(item.getId(), now);
            comments = commentRepository.findAllByItem_IdOrderById(item.getId());
            itemInfoDtos.add(ItemDtoMapper.toItemInfoDto(item, lastBooking, nextBooking, comments));
        }
        return itemInfoDtos;
    }

    @Override
    public ItemInfoDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Вещи с таким ID не существует"));
        LocalDateTime now = LocalDateTime.now();
        Booking lastBooking = null;
        Booking nextBooking = null;
        if (item.getOwner().getId().equals(userId)) {
            lastBooking = bookingRepository.findFirstByItem_IdAndEndBeforeOrderByEndDesc(itemId, now);
            nextBooking = bookingRepository.findFirstByItem_IdAndStartAfterOrderByStartAsc(itemId, now);
        }
        List<Comment> comments = commentRepository.findAllByItem_IdOrderById(itemId);
        return ItemDtoMapper.toItemInfoDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<Item> searchBy(String word) {
        if (word.isEmpty() || word.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchBy(word);
    }

    @Override
    public Comment createComment(Comment comment, Long userId, Long itemId) {
        Item item = findItem(itemId);
        User user = checkUser(userId);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        Booking foundedBooking = bookingRepository.findFirstByItem_IdAndEndBeforeOrderByEndDesc(itemId, LocalDateTime.now());
        if (Objects.isNull(foundedBooking)) {
            throw new EntityCreateException("Оставить комментарий к незабронированной вещи невозможно");
        }
        return commentRepository.save(comment);
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
        Item foundedItem = findItem(itemId);
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

    private Item findItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Вещи с ID %d не найдено", itemId)));
    }
}