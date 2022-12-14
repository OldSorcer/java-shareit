package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.EntityCreateException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    private User user;
    private Item item;
    private Comment comment;

    @BeforeEach
    void beforeEach() {
        itemService = new ItemServiceImpl(itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemRequestRepository);
        user = new User(1L, "User", "user@email.ru");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        item = Item.builder().id(1L).name("Item").description("Description").available(true).owner(user).build();
        item.setRequest(itemRequest);
        comment = Comment.builder().text("Comment").build();
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfCreateItemWithNotExistsRequestId() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception exc = assertThrows(EntityNotFoundException.class,
                () -> itemService.createItem(item, 1L));
        assertEquals("?????????????? ?? ID 1 ???? ????????????????????", exc.getMessage());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfItemNotFound() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception exc = assertThrows(EntityNotFoundException.class,
                () -> itemService.getItemById(1L, 1L));
        assertEquals("???????? ?? ?????????? ID ???? ????????????????????", exc.getMessage());
    }

    @Test
    void shouldReturnEmptyListIfTextForSearchIsEmpty() {
        List<Item> result = itemService.searchBy("", 0, 10);
        assertEquals(0, result.size());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowEntityCreateExceptionIfBookingNotFoundForCreateComment() {
        when(bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(anyLong(), any()))
                .thenReturn(null);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        Exception exc = assertThrows(EntityCreateException.class,
                () -> itemService.createComment(comment, 1L, 1L));
        assertEquals("???????????????? ?????????????????????? ?? ?????????????????????????????????? ???????? ????????????????????", exc.getMessage());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfUserNotFound() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception exc = assertThrows(EntityNotFoundException.class, () -> itemService.createItem(item, 1L));
        assertEquals("???????????????????????? ?? ID 1 ???? ????????????????????", exc.getMessage());
    }

    @Test
    void shouldThrowInvalidArgumentExceptionIfUserIdIsNull() {
        Exception exc = assertThrows(InvalidArgumentException.class, () -> itemService.createItem(item, null));
        assertEquals("?????????????????????????? ?????????????????? ???????? ???? ????????????????????", exc.getMessage());
    }

    @Test
    void shouldExecuteDeleteMethod() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        itemService.deleteItemById(1L, 1L);
        verify(itemRepository, times(1)).deleteById(1L);
    }
}