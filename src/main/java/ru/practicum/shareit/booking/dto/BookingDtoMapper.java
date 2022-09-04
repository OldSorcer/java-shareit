package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public final class BookingDtoMapper {
    public static Booking toBooking(BookingDto bookingDto, User booker, Item item) {
        return Booking.builder().start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(booker)
                .build();
    }

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto.Item item = new BookingDto.Item();
        item.setId(booking.getItem().getId());
        item.setName(booking.getItem().getName());
        BookingDto.User user = new BookingDto.User();
        user.setId(booking.getBooker().getId());
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(item)
                .booker(user)
                .itemId(item.getId())
                .build();
    }
}