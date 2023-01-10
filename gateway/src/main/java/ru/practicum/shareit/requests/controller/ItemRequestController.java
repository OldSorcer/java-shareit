package ru.practicum.shareit.requests.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.client.RequestClient;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Slf4j
public class ItemRequestController {
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                                @RequestHeader(USER_ID_HEADER) Long userId) {
        log.info(
                "[x] ShateIt-gateway: Получен POST запрос к эндпоинту /requests"
        );
        return requestClient.create(userId,itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findUserRequests(@RequestHeader(USER_ID_HEADER) Long userId) {
        log.info(
                "[x] ShateIt-gateway: Получен GET запрос к эндпоинту /requests"
        );
        return requestClient.findUserRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllRequests(@RequestHeader(USER_ID_HEADER) Long userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "20") int size) {
        log.info(
                "[x] ShateIt-gateway: Получен GET запрос к эндпоинту /requests/all/{}/{}", from, size
        );
        return requestClient.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findRequestById(@RequestHeader(USER_ID_HEADER) Long userId,
                                          @PathVariable Long requestId) {
        log.info(
                "[x] ShateIt-gateway: Получен GET запрос к эндпоинту /requests/{}", requestId
        );
        return requestClient.findById(userId, requestId);
    }
}