package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {
    private final LocalDateTime now = LocalDateTime.now();
    private User user1 = new User(null, "User1", "user1@email.ru");
    private User user2 = new User(null, "User2", "user2@email.ru");
    private User user3 = new User(null, "User3", "user3@email.ru");
    private Item item;
    private Item item2;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    private Booking booking4;
    private Booking booking5;
    private Booking booking6;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);
        item = Item.builder()
                .name("Item")
                .description("Description")
                .available(true)
                .owner(user1)
                .build();
        item2 = Item.builder()
                .name("Item")
                .description("Description")
                .available(true)
                .owner(user3)
                .build();
        item = itemRepository.save(item);
        item2 = itemRepository.save(item2);
        booking1 = Booking.builder()
                .item(item)
                .start(now.plusDays(4L))
                .end(now.plusDays(8L))
                .booker(user1)
                .item(item)
                .status(BookingStatus.WAITING)
                .build();
        booking2 = Booking.builder()
                .item(item)
                .start(now.plusDays(8L))
                .end(now.plusDays(12L))
                .booker(user2)
                .item(item)
                .status(BookingStatus.APPROVED)
                .build();
        booking3 = Booking.builder()
                .item(item)
                .start(now.plusDays(4L))
                .end(now.plusDays(8L))
                .booker(user3)
                .item(item2)
                .status(BookingStatus.REJECTED)
                .build();
        booking4 = Booking.builder()
                .item(item)
                .start(now.plusDays(8L))
                .end(now.plusDays(12L))
                .booker(user1)
                .item(item2)
                .status(BookingStatus.REJECTED)
                .build();
        booking5 = Booking.builder()
                .item(item)
                .start(now.plusDays(2L))
                .end(now.plusDays(4L))
                .booker(user2)
                .item(item2)
                .status(BookingStatus.WAITING)
                .build();
        booking6 = Booking.builder()
                .item(item)
                .start(now.plusDays(6L))
                .end(now.plusDays(8L))
                .booker(user3)
                .item(item2)
                .status(BookingStatus.APPROVED)
                .build();
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);
        booking4 = bookingRepository.save(booking4);
        booking5 = bookingRepository.save(booking5);
        booking6 = bookingRepository.save(booking6);
    }

    @AfterEach
    void afterEach() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnFoundedBookingByBookerId() {
        List<Booking> result = bookingRepository.findAllByBookerIdOrderByStartDesc(user2.getId(),
                Pageable.unpaged());
        assertEquals(2, result.size());
        assertEquals(booking2.getId(), result.get(0).getId());
        assertEquals(booking5.getId(), result.get(1).getId());
    }

    @Test
    void shouldReturnBookingByIdAndEndTimeBeforeLocalDateTimeNowPlus9Days() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndEndBeforeOrderByEndDesc(user1.getId(),
                now.plusDays(9),
                Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking1.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindBookingByBookerIdAndStartTimeAfterCurrentTimePlus2Days() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(user1.getId(),
                now.plusDays(2),
                Pageable.unpaged());
        List<Booking> expected = List.of(booking4, booking1);
        assertEquals(2, result.size());
        assertEquals(expected.get(0).getId(), result.get(0).getId());
        assertEquals(expected.get(1).getId(), result.get(1).getId());
    }

    @Test
    void shouldFindAllByBookerIdAndStatus() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndStatusOrderByStartAsc(user1.getId(),
                BookingStatus.REJECTED,
                Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking4.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindByItemOwnerId() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(user3.getId(),
                Pageable.unpaged());
        assertEquals(4, result.size());
        assertEquals(booking4.getId(), result.get(0).getId());
        assertEquals(booking6.getId(), result.get(1).getId());
        assertEquals(booking3.getId(), result.get(2).getId());
    }

    @Test
    void shouldFindByItemOwnerIdIfEndBeforeCurrentTimePlus9Days() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(user1.getId(),
                now.plusDays(9),
                Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking1.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindByItemOwnerIdIfStartAfter7Days() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(user1.getId(),
                now.plusDays(7),
                Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking2.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindByItemOwnerIdIfBookingStatusWaiting() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartAsc(user1.getId(),
                BookingStatus.WAITING, Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking1.getId(), result.get(0).getId());
        assertEquals(booking1.getStatus(), result.get(0).getStatus());
    }

    @Test
    void shouldFindByItemIdIfEndBeforeCurrentTimePlus13Days() {
        Booking result = bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(item2.getId(), now.plusDays(13));
        assertEquals(booking4.getId(), result.getId());
        assertEquals(booking4.getEnd(), result.getEnd());
    }

    @Test
    void shouldFindByItemIdIfStartAfterCurrentTimePlus7Days() {
        Booking result = bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(item2.getId(),
                now.plusDays(7));
        assertEquals(booking4.getId(), result.getId());
        assertEquals(booking4.getStart(), result.getStart());
    }

    @Test
    void shouldFindByBookerIdIfStartBeforeCurrentTimePlus9DaysAndEndAfterCurrentTimePlus11Days() {
        List<Booking> result = bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(user1.getId(),
                now.plusDays(9),
                now.plusDays(11),
                Pageable.unpaged());
        assertEquals(1L, result.size());
        assertEquals(booking4.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindByItemOwnerIdIfStartBeforeCurrentTimePlus9DaysAndEndAfterCurrentTimePlus11Days() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(user1.getId(),
                now.plusDays(9),
                now.plusDays(11),
                Pageable.unpaged());
        assertEquals(1L, result.size());
        assertEquals(booking2.getId(), result.get(0).getId());
    }
}