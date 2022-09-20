package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingDtoMapperTest {
    private static User user;
    private static Item item;
    private static Booking booking;
    private static BookingDto bookingDto;

    @BeforeAll
    static void beforeAll() {
        user = new User(1L, "User", "user@email.ru");
        item = Item.builder().id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .owner(user).build();
        booking = Booking.builder().item(item)
                .id(1L)
                .start(LocalDateTime.of(2001, 11, 3, 3, 3))
                .end(LocalDateTime.of(2001, 11, 3, 3, 3))
                .booker(user)
                .item(item)
                .status(BookingStatus.WAITING).build();
        BookingDto.User userDto = new BookingDto.User(user);
        BookingDto.Item itemDto = new BookingDto.Item(item);
        bookingDto = BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(itemDto)
                .booker(userDto)
                .itemId(item.getId())
                .build();
    }

    @Test
    void shouldReturnBookingDto() {
        BookingDto result = BookingDtoMapper.toBookingDto(booking);
        assertEquals(bookingDto, result);
    }

    @Test
    void shouldReturnBooking() {
        Booking result = BookingDtoMapper.toBooking(bookingDto);
        assertEquals(booking.getStart(), result.getStart());
        assertEquals(booking.getEnd(), result.getEnd());
    }

    @Test
    void shouldReturnListOfBookingDto() {
        List<BookingDto> expected = List.of(bookingDto);
        List<BookingDto> result = BookingDtoMapper.toBookingDto(List.of(booking));
        assertEquals(expected, result);
    }
}