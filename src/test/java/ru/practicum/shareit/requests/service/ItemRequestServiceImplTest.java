package ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    private final User user = new User(1L, "User", "user@email.ru");
    private final ItemRequest itemRequest = new ItemRequest(1L, "Description", user, LocalDateTime.now());
    private ItemRequestService itemRequestService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    void beforeEach() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository, itemRepository);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfUserNotExistWhenItemRequestCreate() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());
        Exception exc = assertThrows(EntityNotFoundException.class, () -> itemRequestService.createRequest(itemRequest, 1L));
        assertEquals("Пользователь с ID 1 не найден", exc.getMessage());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfRequestDoesNotExist() {
        when(itemRequestRepository.findById(1L))
                .thenReturn(Optional.empty());
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Exception exc = assertThrows(EntityNotFoundException.class, () -> itemRequestService.findRequestById(1L, 1L));
        assertEquals("Запрос с ID 1 не найден", exc.getMessage());
    }
}