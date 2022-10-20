package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Slf4j
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto,
                              @RequestHeader(USER_ID_HEADER) Long ownerId) {
        log.info(
                "[x] ShateIt-server: Получен POST запрос к эндпоинту /items"
        );
        Item createdItem = itemService.createItem(ItemDtoMapper.toItem(itemDto), ownerId);
        return ItemDtoMapper.toItemDto(createdItem);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader(USER_ID_HEADER) Long ownerId) {
        log.info(
                "[x] ShateIt-server: Получен PATCH запрос к эндпоинту /items/{}", itemId
        );
        Item updatedItem = itemService.updateItem(itemId, ItemDtoMapper.toItem(itemDto), ownerId);
        return ItemDtoMapper.toItemDto(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ItemInfoDto getItemById(@PathVariable Long itemId, @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info(
                "[x] ShateIt-server: Получен GET запрос к эндпоинту /items/{}", itemId
        );
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemInfoDto> getItemsByOwnerId(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                               @RequestParam(defaultValue = "0", required = false) int from,
                                               @RequestParam(defaultValue = "10", required = false) int size) {
        log.info(
                "[x] ShateIt-server: Получен GET запрос к эндпоинту /items/{}/{}", from, size
        );
        return itemService.getItemByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> searchBy(@RequestParam String text,
                                  @RequestParam(defaultValue = "0", required = false) int from,
                                  @RequestParam(defaultValue = "10", required = false) int size) {
        log.info(
                "[x] ShateIt-server: Получен GET запрос к эндпоинту /items/search/{}/{}/{}",
                text,
                from,
                size
        );
        return ItemDtoMapper.toItemDto(itemService.searchBy(text, from, size));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentDto commentDto,
                                    @RequestHeader(USER_ID_HEADER) Long userId,
                                    @PathVariable Long itemId) {
        log.info(
                "[x] ShateIt-server: Получен POST запрос к эндпоинту /items/comment"
        );
        Comment createdComment = itemService.createComment(CommentDtoMapper.toComment(commentDto), userId, itemId);
        return CommentDtoMapper.toCommentDto(createdComment);
    }
}