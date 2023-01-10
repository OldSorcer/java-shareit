package ru.practicum.shareit.requests.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.requests.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Slf4j
public class ItemRequestController {
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestBody ItemRequestDto itemRequestDto,
                                        @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info(
                "[x] ShateIt-server: Получен POST запрос к эндпоинту /requests"
        );
        return itemRequestService.createRequest(ItemRequestDtoMapper.toItemRequest(itemRequestDto), userId);
    }

    @GetMapping
    public List<ItemRequestDto> findUserRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info(
                "[x] ShateIt-server: Получен GET запрос к эндпоинту /requests"
        );
        return itemRequestService.findUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllRequests(@RequestHeader(USER_ID_HEADER) Long userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "20") int size) {
        log.info(
                "[x] ShateIt-server: Получен GET запрос к эндпоинту /requests/all/{}/{}", from, size
        );
        return itemRequestService.findAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findRequestById(@RequestHeader(USER_ID_HEADER) Long userId,
                                          @PathVariable Long requestId) {
        log.info(
                "[x] ShateIt-server: Получен GET запрос к эндпоинту /requests/{}", requestId
        );
        return itemRequestService.findRequestById(userId, requestId);
    }
}