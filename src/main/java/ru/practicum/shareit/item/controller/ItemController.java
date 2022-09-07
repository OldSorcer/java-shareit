package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validator.groups.Create;

import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;
    private final BookingService bookingService;

    @PostMapping
    public ItemDto createItem(@RequestBody @Validated({Create.class}) ItemDto itemDto,
                              @RequestHeader(USER_ID_HEADER) Long ownerId) {
        Item createdItem = itemService.createItem(ItemDtoMapper.toItem(itemDto), ownerId);
        return ItemDtoMapper.toItemDto(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader(USER_ID_HEADER) Long ownerId) {
        Item updatedItem = itemService.updateItem(itemId, ItemDtoMapper.toItem(itemDto), ownerId);
        return ItemDtoMapper.toItemDto(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ItemInfoDto getItemById(@PathVariable Long itemId, @RequestHeader(USER_ID_HEADER) Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemInfoDto> getItemsByOwnerId(@RequestHeader(USER_ID_HEADER) Long ownerId) {
        return itemService.getItemByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchBy(@RequestParam String text) {
        return ItemDtoMapper.toItemDto(itemService.searchBy(text));
    }
}