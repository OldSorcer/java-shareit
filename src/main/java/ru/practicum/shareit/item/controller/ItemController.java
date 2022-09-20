package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validator.groups.Create;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
    public List<ItemInfoDto> getItemsByOwnerId(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                               @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                               @Positive @RequestParam(defaultValue = "10", required = false) int size) {
        return itemService.getItemByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchBy(@RequestParam String text,
                                  @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                  @Positive @RequestParam(defaultValue = "10", required = false) int size) {
        return ItemDtoMapper.toItemDto(itemService.searchBy(text, from, size));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto,
                                    @RequestHeader(USER_ID_HEADER) Long userId,
                                    @PathVariable Long itemId) {
        Comment createdComment = itemService.createComment(CommentDtoMapper.toComment(commentDto), userId, itemId);
        return CommentDtoMapper.toCommentDto(createdComment);
    }
}