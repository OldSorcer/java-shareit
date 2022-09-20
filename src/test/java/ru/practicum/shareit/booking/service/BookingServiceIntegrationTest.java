package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceIntegrationTest {
    @Autowired
    private final UserService userService;
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final BookingService bookingService;
    private User user = new User(null, "User", "user@email.com");
    private User booker = new User(null, "Booker", "booker@emaul.com");
    private Item item = new Item(null, "Item", "Description", true, null, null);
    private Booking booking = new Booking(null, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), null, null, null);

    @Test
    void approveBooking() {
        User createdUser = userService.createUser(user);
        User createdBooker = userService.createUser(booker);
        Item createdItem = itemService.createItem(item, createdUser.getId());
        BookingDto createdBooking = bookingService.createBooking(booking, createdBooker.getId(), createdItem.getId());
        BookingDto approvedBooking = bookingService.approveBooking(createdBooking.getId(), true, user.getId());
        assertEquals(BookingStatus.APPROVED, approvedBooking.getStatus());
    }

    @Test
    void getBookings() {
        User createdUser = userService.createUser(user);
        User createdBooker = userService.createUser(booker);
        Item createdItem = itemService.createItem(item, createdUser.getId());
        BookingDto createdBooking = bookingService.createBooking(booking, createdBooker.getId(), createdItem.getId());
        List<BookingDto> result = bookingService.getBookings(BookingState.ALL, createdBooker.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
    }

    @Test
    void getBookingsByOwnerId() {
        User createdUser = userService.createUser(user);
        User createdBooker = userService.createUser(booker);
        Item createdItem = itemService.createItem(item, createdUser.getId());
        BookingDto createdBooking = bookingService.createBooking(booking, createdBooker.getId(), createdItem.getId());
        List<BookingDto> result = bookingService.getBookingsByOwnerId(BookingState.ALL, createdUser.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
    }

    @Test
    void getFutureBookings() {
        User createdUser = userService.createUser(user);
        User createdBooker = userService.createUser(booker);
        Item createdItem = itemService.createItem(item, createdUser.getId());
        BookingDto createdBooking = bookingService.createBooking(booking, createdBooker.getId(), createdItem.getId());
        List<BookingDto> result = bookingService.getBookings(BookingState.FUTURE, createdBooker.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
    }

    @Test
    void getRejectedBookings() {
        User createdUser = userService.createUser(user);
        User createdBooker = userService.createUser(booker);
        Item createdItem = itemService.createItem(item, createdUser.getId());
        BookingDto createdBooking = bookingService.createBooking(booking, createdBooker.getId(), createdItem.getId());
        BookingDto rejectedBooking = bookingService.approveBooking(createdBooking.getId(), false, createdUser.getId());
        List<BookingDto> result = bookingService.getBookings(BookingState.REJECTED, createdBooker.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
        assertEquals(rejectedBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getWaitingBooking() {
        User createdUser = userService.createUser(user);
        User createdBooker = userService.createUser(booker);
        Item createdItem = itemService.createItem(item, createdUser.getId());
        BookingDto createdBooking = bookingService.createBooking(booking, createdBooker.getId(), createdItem.getId());
        List<BookingDto> result = bookingService.getBookings(BookingState.WAITING, createdBooker.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
        assertEquals(createdBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getRejectedBookingsByOwnerId() {
        User createdUser = userService.createUser(user);
        User createdBooker = userService.createUser(booker);
        Item createdItem = itemService.createItem(item, createdUser.getId());
        BookingDto createdBooking = bookingService.createBooking(booking, createdBooker.getId(), createdItem.getId());
        BookingDto rejectedBooking = bookingService.approveBooking(createdBooking.getId(), false, createdUser.getId());
        List<BookingDto> result = bookingService.getBookingsByOwnerId(BookingState.REJECTED, createdUser.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(rejectedBooking.getId(), result.get(0).getId());
        assertEquals(rejectedBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getFutureBookingsByOwnerId() {
        User createdUser = userService.createUser(user);
        User createdBooker = userService.createUser(booker);
        Item createdItem = itemService.createItem(item, createdUser.getId());
        BookingDto createdBooking = bookingService.createBooking(booking, createdBooker.getId(), createdItem.getId());
        List<BookingDto> result = bookingService.getBookingsByOwnerId(BookingState.FUTURE, createdUser.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
    }

    @Test
    void getWaitingBookingsByOwnerId() {
        User createdUser = userService.createUser(user);
        User createdBooker = userService.createUser(booker);
        Item createdItem = itemService.createItem(item, createdUser.getId());
        BookingDto createdBooking = bookingService.createBooking(booking, createdBooker.getId(), createdItem.getId());
        List<BookingDto> result = bookingService.getBookingsByOwnerId(BookingState.WAITING, createdUser.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
        assertEquals(createdBooking.getStatus(), result.get(0).getStatus());
    }
}