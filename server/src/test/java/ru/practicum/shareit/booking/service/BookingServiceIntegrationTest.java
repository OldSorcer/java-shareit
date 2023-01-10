package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    private final Booking booking = new Booking(null, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), null, null, null);
    private User user = new User(null, "User", "user@email.com");
    private User booker = new User(null, "Booker", "booker@emaul.com");
    private Item item = new Item(null, "Item", "Description", true, null, null);
    private BookingInfoDto createdBooking;

    @BeforeEach
    void beforeEach() {
        user = userService.createUser(user);
        booker = userService.createUser(booker);
        item = itemService.createItem(item, user.getId());
        createdBooking = bookingService.createBooking(booking, booker.getId(), item.getId());
    }

    @Test
    void approveBooking() {
        BookingInfoDto approvedBooking = bookingService.approveBooking(createdBooking.getId(), true, user.getId());
        assertEquals(BookingStatus.APPROVED, approvedBooking.getStatus());
    }

    @Test
    void getBookings() {
        List<BookingInfoDto> result = bookingService.getBookings(BookingState.ALL, booker.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
    }

    @Test
    void getBookingsByOwnerId() {
        List<BookingInfoDto> result = bookingService.getBookingsByOwnerId(BookingState.ALL, user.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
    }

    @Test
    void getFutureBookings() {
        List<BookingInfoDto> result = bookingService.getBookings(BookingState.FUTURE, booker.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
    }

    @Test
    void getRejectedBookings() {
        BookingInfoDto rejectedBooking = bookingService.approveBooking(createdBooking.getId(), false, user.getId());
        List<BookingInfoDto> result = bookingService.getBookings(BookingState.REJECTED, booker.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
        assertEquals(rejectedBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getWaitingBooking() {
        List<BookingInfoDto> result = bookingService.getBookings(BookingState.WAITING, booker.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
        assertEquals(createdBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getRejectedBookingsByOwnerId() {
        BookingInfoDto rejectedBooking = bookingService.approveBooking(createdBooking.getId(), false, user.getId());
        List<BookingInfoDto> result = bookingService.getBookingsByOwnerId(BookingState.REJECTED, user.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(rejectedBooking.getId(), result.get(0).getId());
        assertEquals(rejectedBooking.getStatus(), result.get(0).getStatus());
    }

    @Test
    void getFutureBookingsByOwnerId() {
        List<BookingInfoDto> result = bookingService.getBookingsByOwnerId(BookingState.FUTURE, user.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
    }

    @Test
    void getWaitingBookingsByOwnerId() {
        List<BookingInfoDto> result = bookingService.getBookingsByOwnerId(BookingState.WAITING, user.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdBooking.getId(), result.get(0).getId());
        assertEquals(createdBooking.getStatus(), result.get(0).getStatus());
    }
}