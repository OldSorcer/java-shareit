package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createRequest(ItemRequest itemRequest, Long userId);
    List<ItemRequestDto> findUserRequests(Long userId);
    List<ItemRequestDto> findAllRequests(Long userId, int from, int size);
    ItemRequestDto findRequestById(Long userId, Long requestId);
}
