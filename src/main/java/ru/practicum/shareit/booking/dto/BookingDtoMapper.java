package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.stream.Collectors;

public final class BookingDtoMapper {
    public static Booking toBooking(BookingDto bookingDto) {
        return Booking.builder().start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();
    }

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto.Item item = new BookingDto.Item(booking.getItem());
        BookingDto.User user = new BookingDto.User(booking.getBooker());
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

    public static List<BookingDto> toBookingDto(List<Booking> bookings) {
        return bookings.stream().map(BookingDtoMapper::toBookingDto).collect(Collectors.toList());
    }
}