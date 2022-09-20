package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.EntityCreateException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    private BookingService bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    private Booking booking;
    private User user;
    private Item item;

    @BeforeEach
    void beforeEach() {
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
        user = new User(1L, "User", "user@email.ru");
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        item = Item.builder().id(1L).name("Item").description("Description").available(true).owner(user).build();
        item.setRequest(itemRequest);
        booking = Booking.builder().item(item).start(LocalDateTime.now().plusDays(1L)).end(LocalDateTime.MAX).build();
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfItemNotFound() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        Exception exc = assertThrows(EntityNotFoundException.class,
                () -> bookingService.createBooking(booking, 1L, 1L));
        assertEquals("Вещи с ID 1 не существует", exc.getMessage());
    }

    @Test
    void shouldThrowEntityCreateExceptionIfBookingEndTimeIsBeforeCurrentTime() {
        Booking failedBooking = Booking.builder().start(LocalDateTime.MAX).end(LocalDateTime.MIN).build();
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Exception exc = assertThrows(EntityCreateException.class,
                () -> bookingService.createBooking(failedBooking, 1L, 1L));
        assertEquals("Некорректное время окончания бронирования", exc.getMessage());
    }

    @Test
    void shouldThrowEntityCreateExceptionIfBookingStartTimeIsBeforeCurrentTime() {
        Booking failedBooking = Booking.builder().start(LocalDateTime.MIN).end(LocalDateTime.MAX).build();
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Exception exc = assertThrows(EntityCreateException.class,
                () -> bookingService.createBooking(failedBooking, 1L, 1L));
        assertEquals("Некорректное время начала бронирования", exc.getMessage());
    }

    @Test
    void shouldThrowEntityCreateExceptionIfBookingStartTimeIsBeforeBookingEndTime() {
        Booking failedBooking = Booking.builder().start(LocalDateTime.now().plusDays(2L)).end(LocalDateTime.now().plusDays(1L)).build();
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Exception exc = assertThrows(EntityCreateException.class,
                () -> bookingService.createBooking(failedBooking, 1L, 1L));
        assertEquals("Время окончания бронирования не может быть раньше времени начала бронирования",
                exc.getMessage());
    }

    @Test
    void shouldThrowEntityCreateExceptionIfBookingIdEqualsItemOwnerId() {
        Booking failedBooking = Booking.builder().start(LocalDateTime.now().plusDays(1L)).end(LocalDateTime.now().plusDays(2L)).booker(user).build();
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Exception exc = assertThrows(EntityNotFoundException.class,
                () -> bookingService.createBooking(failedBooking, 1L, 1L));
        assertEquals("Вы являетесь владельцем предмета и не можете создать бронирование",
                exc.getMessage());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfBookerIdEqualsItemOwnerId() {
        booking.setBooker(user);
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));
        Exception exc = assertThrows(EntityNotFoundException.class, () -> bookingService.approveBooking(1L, true, 2L));
        assertEquals("Вы не можете подтверждать или отклонять бронирование вещи, владельцем которой не являетесь", exc.getMessage());
    }

    @Test
    void shouldThrowEntityCreateExceptionIfBookingAlreadyApproved() {
        booking.setBooker(user);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Exception exc = assertThrows(EntityCreateException.class, () -> bookingService.approveBooking(1L, true, 1L));
        assertEquals("Бронирование уже подтверждено", exc.getMessage());
    }

    @Test
    void shouldThrowEntityCreateExceptionIfBookingIsAlreadyRejected() {
        booking.setBooker(user);
        booking.setStatus(BookingStatus.REJECTED);
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Exception exc = assertThrows(EntityCreateException.class, () -> bookingService.approveBooking(1L, true, 1L));
        assertEquals("Бронирование уже отклонено", exc.getMessage());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfBookingNotFoundById() {
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.empty());
        Exception exc = assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
        assertEquals("Бронирования с ID 1 не существует", exc.getMessage());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfTryToGetBookingWithDoesNotExistsUserId() {
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());
        Exception exc = assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
        assertEquals("Пользователя с ID 1 не существует", exc.getMessage());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfBookingAccessDenied() {
        booking.setBooker(user);
        when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(booking));
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(user));
        Exception exc = assertThrows(EntityNotFoundException.class, () -> bookingService.getBookingById(1L, 2L));
        assertEquals("Вы не можете проматривать или редактировать информацию о " +
                "бронировании, так как не являетесь владельцем вещи или автором бронирования", exc.getMessage());
    }

    @Test
    void shouldThrowEntityCreateExceptionIfItemIsNotAvailable() {
        item.setAvailable(false);
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        Exception exc = assertThrows(EntityCreateException.class, () -> bookingService.createBooking(booking, 1L, 1L));
        assertEquals("Вещь с ID 1 недоступна", exc.getMessage());
    }
}