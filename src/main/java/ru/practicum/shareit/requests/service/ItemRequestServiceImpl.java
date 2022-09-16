package ru.practicum.shareit.requests.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService{
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto createRequest(ItemRequest itemRequest, Long userId) {
        User requester = findUserById(userId);
        itemRequest.setRequester(requester);
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestDtoMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> findUserRequests(Long userId) {
        findUserById(userId);
        return itemRequestRepository.findAllByRequesterIdOrderById(userId).stream()
                .map(itemRequest -> ItemRequestDtoMapper
                        .toItemRequestDto(itemRequest, itemRepository.findByRequestId(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> findAllRequests(Long userId, int from, int size) {
        findUserById(userId);
        Pageable page = PageRequest.of(from, size, Sort.by("created").descending());
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequesterIdNot(userId, page);
        return itemRequests.stream()
                .map(itemRequest -> ItemRequestDtoMapper
                        .toItemRequestDto(itemRequest, itemRepository.findByRequestId(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto findRequestById(Long userId, Long requestId) {
        findUserById(userId);
        ItemRequest foundedRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Запрос с ID %d не найден", requestId)));
        List<Item> foundedItems = itemRepository.findByRequestId(foundedRequest.getId());
        return ItemRequestDtoMapper.toItemRequestDto(foundedRequest, foundedItems);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с ID %d не найден",
                        userId)));
    }
}