package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingRepositoryTest {
    private final User user1 = new User(1L, "User1", "user1@email.ru");
    private final User user2 = new User(2L, "User2", "user2@email.ru");
    private final User user3 = new User(3L, "User3", "user3@email.ru");
    private final Item item = Item.builder().id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(user1).build();
    private final Booking booking1 = Booking.builder().item(item)
            .id(1L)
            .start(LocalDateTime.now().plusDays(4L))
            .end(LocalDateTime.now().plusDays(8L))
            .booker(user1)
            .item(item)
            .status(BookingStatus.WAITING).build();
    private final Booking booking2 = Booking.builder().item(item)
            .id(2L)
            .start(LocalDateTime.now().plusDays(8L))
            .end(LocalDateTime.now().plusDays(12L))
            .booker(user2)
            .item(item)
            .status(BookingStatus.APPROVED).build();
    private final Item item2 = Item.builder().id(2L)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(user3).build();
    private final Booking booking3 = Booking.builder().item(item)
            .id(3L)
            .start(LocalDateTime.now().plusDays(4L))
            .end(LocalDateTime.now().plusDays(8L))
            .booker(user3)
            .item(item2)
            .status(BookingStatus.REJECTED).build();

    private final Booking booking4 = Booking.builder().item(item)
            .id(4L)
            .start(LocalDateTime.now().plusDays(8L))
            .end(LocalDateTime.now().plusDays(12L))
            .booker(user1)
            .item(item2)
            .status(BookingStatus.REJECTED).build();

    private final Booking booking5 = Booking.builder().item(item)
            .id(5L)
            .start(LocalDateTime.now().plusDays(2L))
            .end(LocalDateTime.now().plusDays(4L))
            .booker(user2)
            .item(item2)
            .status(BookingStatus.WAITING).build();

    private final Booking booking6 = Booking.builder().item(item)
            .id(6L)
            .start(LocalDateTime.now().plusDays(6L))
            .end(LocalDateTime.now().plusDays(8L))
            .booker(user3)
            .item(item2)
            .status(BookingStatus.APPROVED).build();
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        itemRepository.save(item);
        itemRepository.save(item2);
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        bookingRepository.save(booking5);
        bookingRepository.save(booking6);
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void shouldReturnFoundedBookingByBookerId() {
        List<Booking> result = bookingRepository.findAllByBookerIdOrderByStartDesc(2L, Pageable.unpaged());
        assertEquals(2, result.size());
        assertEquals(booking2.getId(), result.get(0).getId());
        assertEquals(booking5.getId(), result.get(1).getId());
    }

    @Test
    void shouldReturnBookingByIdAndEndTimeBeforeLocalDateTimeNowPlus9Days() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndEndBeforeOrderByEndDesc(1L, LocalDateTime.now().plusDays(9), Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking1.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindBookingByBookerIdAndStartTimeAfterCurrentTimePlus2Days() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(1L, LocalDateTime.now().plusDays(2), Pageable.unpaged());
        List<Booking> expected = List.of(booking4, booking1);
        assertEquals(2, result.size());
        assertEquals(expected.get(0).getId(), result.get(0).getId());
        assertEquals(expected.get(1).getId(), result.get(1).getId());
    }

    @Test
    void shouldFindAllByBookerIdAndStatus() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndStatusOrderByStartAsc(1L, BookingStatus.REJECTED, Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking4.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindByItemOwnerId() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(3L, Pageable.unpaged());
        assertEquals(4, result.size());
        assertEquals(booking4.getId(), result.get(0).getId());
        assertEquals(booking6.getId(), result.get(1).getId());
        assertEquals(booking3.getId(), result.get(2).getId());
    }

    @Test
    void shouldFindByItemOwnerIdAndEndBeforeCurrentTimePlus9Days() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(1L, LocalDateTime.now().plusDays(9), Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking1.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindByItemOwnerIdAndStartAfter7Days() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(1L, LocalDateTime.now().plusDays(7), Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking2.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindByItemOwnerIdAndBookingStatusWaiting() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartAsc(1L, BookingStatus.WAITING, Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking1.getId(), result.get(0).getId());
        assertEquals(booking1.getStatus(), result.get(0).getStatus());
    }

    @Test
    void shouldFindByItemIdAndEndBeforeCurrentTimePlus13Days() {
        Booking result = bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(2L, LocalDateTime.now().plusDays(13));
        assertEquals(4, result.getId());
        assertEquals(booking4.getEnd(), result.getEnd());
    }

    @Test
    void shouldFindByItemIdAndStartAfterCurrentTimePlus7Days() {
        Booking result = bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(2L, LocalDateTime.now().plusDays(7));
        assertEquals(4, result.getId());
        assertEquals(booking4.getStart(), result.getStart());
    }

    @Test
    void shouldFindByBookerIdAndStartBeforeCurrentTimePlus9DaysAndEndAfterCurrentTimePlus11Days() {
        List<Booking> result = bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(1L, LocalDateTime.now().plusDays(9), LocalDateTime.now().plusDays(11), Pageable.unpaged());
        assertEquals(1L, result.size());
        assertEquals(booking4.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindByItemOwnerIdAndStartBeforeCurrentTimePlus9DaysAndEndAfterCurrentTimePlus11Days() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(1L, LocalDateTime.now().plusDays(9), LocalDateTime.now().plusDays(11), Pageable.unpaged());
        assertEquals(1L, result.size());
        assertEquals(booking2.getId(), result.get(0).getId());
    }
}