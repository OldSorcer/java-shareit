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
    private User user1 = new User(1L, "User1", "user1@email.ru");
    private User user2 = new User(2L, "User2", "user2@email.ru");
    private User user3 = new User(3L, "User3", "user3@email.ru");
    private Item item = Item.builder().id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(user1).build();
    private Booking booking1 = Booking.builder().item(item)
            .id(1L)
            .start(LocalDateTime.now().plusDays(4L))
            .end(LocalDateTime.now().plusDays(8L))
            .booker(user1)
            .item(item)
            .status(BookingStatus.WAITING).build();
    private Booking booking2 = Booking.builder().item(item)
            .id(2L)
            .start(LocalDateTime.now().plusDays(8L))
            .end(LocalDateTime.now().plusDays(12L))
            .booker(user2)
            .item(item)
            .status(BookingStatus.APPROVED).build();
    private Item item2 = Item.builder().id(2L)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(user3).build();
    private Booking booking3 = Booking.builder().item(item)
            .id(3L)
            .start(LocalDateTime.now().plusDays(4L))
            .end(LocalDateTime.now().plusDays(8L))
            .booker(user3)
            .item(item2)
            .status(BookingStatus.REJECTED).build();

    private Booking booking4 = Booking.builder().item(item)
            .id(4L)
            .start(LocalDateTime.now().plusDays(8L))
            .end(LocalDateTime.now().plusDays(12L))
            .booker(user1)
            .item(item2)
            .status(BookingStatus.REJECTED).build();

    private Booking booking5 = Booking.builder().item(item)
            .id(5L)
            .start(LocalDateTime.now().plusDays(2L))
            .end(LocalDateTime.now().plusDays(4L))
            .booker(user2)
            .item(item2)
            .status(BookingStatus.WAITING).build();

    private Booking booking6 = Booking.builder().item(item)
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
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);
        item = itemRepository.save(item);
        item2 = itemRepository.save(item2);
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
        List<Booking> result = bookingRepository.findAllByBookerIdOrderByStartDesc(user2.getId(), Pageable.unpaged());
        assertEquals(2, result.size());
        assertEquals(booking2.getId(), result.get(0).getId());
        assertEquals(booking5.getId(), result.get(1).getId());
    }

    @Test
    void shouldReturnBookingByIdAndEndTimeBeforeLocalDateTimeNowPlus9Days() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndEndBeforeOrderByEndDesc(user1.getId(), LocalDateTime.now().plusDays(9), Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking1.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindBookingByBookerIdAndStartTimeAfterCurrentTimePlus2Days() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(user1.getId(), LocalDateTime.now().plusDays(2), Pageable.unpaged());
        List<Booking> expected = List.of(booking4, booking1);
        assertEquals(2, result.size());
        assertEquals(expected.get(0).getId(), result.get(0).getId());
        assertEquals(expected.get(1).getId(), result.get(1).getId());
    }

    @Test
    void shouldFindAllByBookerIdAndStatus() {
        List<Booking> result = bookingRepository.findAllByBookerIdAndStatusOrderByStartAsc(user1.getId(), BookingStatus.REJECTED, Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking4.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindByItemOwnerId() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(user3.getId(), Pageable.unpaged());
        assertEquals(4, result.size());
        assertEquals(booking4.getId(), result.get(0).getId());
        assertEquals(booking6.getId(), result.get(1).getId());
        assertEquals(booking3.getId(), result.get(2).getId());
    }

    @Test
    void shouldFindByItemOwnerIdAndEndBeforeCurrentTimePlus9Days() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(user1.getId(), LocalDateTime.now().plusDays(9), Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking1.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindByItemOwnerIdAndStartAfter7Days() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(user1.getId(), LocalDateTime.now().plusDays(7), Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking2.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindByItemOwnerIdAndBookingStatusWaiting() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartAsc(user1.getId(), BookingStatus.WAITING, Pageable.unpaged());
        assertEquals(1, result.size());
        assertEquals(booking1.getId(), result.get(0).getId());
        assertEquals(booking1.getStatus(), result.get(0).getStatus());
    }

    @Test
    void shouldFindByItemIdAndEndBeforeCurrentTimePlus13Days() {
        Booking result = bookingRepository.findFirstByItemIdAndEndBeforeOrderByEndDesc(item2.getId(), LocalDateTime.now().plusDays(13));
        assertEquals(4, result.getId());
        assertEquals(booking4.getEnd(), result.getEnd());
    }

    @Test
    void shouldFindByItemIdAndStartAfterCurrentTimePlus7Days() {
        Booking result = bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(item2.getId(), LocalDateTime.now().plusDays(7));
        assertEquals(4, result.getId());
        assertEquals(booking4.getStart(), result.getStart());
    }

    @Test
    void shouldFindByBookerIdAndStartBeforeCurrentTimePlus9DaysAndEndAfterCurrentTimePlus11Days() {
        List<Booking> result = bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(booking1.getId(), LocalDateTime.now().plusDays(9), LocalDateTime.now().plusDays(11), Pageable.unpaged());
        assertEquals(1L, result.size());
        assertEquals(booking4.getId(), result.get(0).getId());
    }

    @Test
    void shouldFindByItemOwnerIdAndStartBeforeCurrentTimePlus9DaysAndEndAfterCurrentTimePlus11Days() {
        List<Booking> result = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(user1.getId(), LocalDateTime.now().plusDays(9), LocalDateTime.now().plusDays(11), Pageable.unpaged());
        assertEquals(1L, result.size());
        assertEquals(booking2.getId(), result.get(0).getId());
    }
}