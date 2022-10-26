package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingDtoMapperTest {
    private static User user;
    private static Item item;
    private static Booking booking;
    private static BookingInfoDto bookingDto;
    private static BookingDto bookingEntryDto;

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
        UserDto userDto = UserDtoMapper.toUserDto(user);
        ItemDto itemDto = ItemDtoMapper.toItemDto(item);
        bookingDto = BookingInfoDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(itemDto)
                .booker(userDto)
                .build();
        bookingEntryDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2001, 11, 3, 3, 3))
                .end(LocalDateTime.of(2001, 12, 3, 3, 3))
                .build();
    }

    @Test
    void shouldReturnBookingDto() {
        BookingInfoDto result = BookingDtoMapper.toBookingInfoDto(booking);
        assertEquals(bookingDto, result);
    }

    @Test
    void shouldReturnBooking() {
        Booking result = BookingDtoMapper.toBooking(bookingEntryDto);
        assertEquals(bookingEntryDto.getStart(), result.getStart());
        assertEquals(bookingEntryDto.getEnd(), result.getEnd());
    }

    @Test
    void shouldReturnListOfBookingDto() {
        List<BookingInfoDto> expected = List.of(bookingDto);
        List<BookingInfoDto> result = BookingDtoMapper.toBookingInfoDto(List.of(booking));
        assertEquals(expected, result);
    }
}