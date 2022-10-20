package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.groups.Create;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Slf4j
public class ItemController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestBody @Validated({Create.class}) ItemDto itemDto,
                                     @RequestHeader(USER_ID_HEADER) Long ownerId) {
        log.info(
                "[x] ShateIt-gateway: Получен POST запрос к эндпоинту /items"
        );
        return itemClient.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader(USER_ID_HEADER) Long ownerId) {
        log.info(
                "[x] ShateIt-gateway: Получен PATCH запрос к эндпоинту /items/{}", itemId
        );
        return itemClient.update(ownerId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId, @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info(
                "[x] ShateIt-gateway: Получен GET запрос к эндпоинту /items/{}", itemId
        );
        return itemClient.getById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByOwnerId(@RequestHeader(USER_ID_HEADER) Long ownerId,
                                               @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                               @Positive @RequestParam(defaultValue = "10", required = false) int size) {
        log.info(
                "[x] ShateIt-gateway: Получен GET запрос к эндпоинту /items"
        );
        return itemClient.getAllByOwnerId(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchBy(@RequestHeader(USER_ID_HEADER) Long userId,
                                           @RequestParam String text,
                                           @PositiveOrZero @RequestParam(defaultValue = "0", required = false) int from,
                                           @Positive @RequestParam(defaultValue = "10", required = false) int size) {
        log.info(
                "[x] ShateIt-gateway: Получен GET запрос к эндпоинту /items/search/{}/{}/{}",
                text,
                from,
                size
        );
        return itemClient.searchBy(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                                    @RequestHeader(USER_ID_HEADER) Long userId,
                                    @PathVariable Long itemId) {
        log.info(
                "[x] ShateIt-gateway: Получен POST запрос к эндпоинту /items/{}/comment", itemId
        );
        return itemClient.createComment(userId, itemId, commentDto);
    }
}