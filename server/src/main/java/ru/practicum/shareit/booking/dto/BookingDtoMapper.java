package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

public final class BookingDtoMapper {
    public static Booking toBooking(BookingDto bookingDto) {
        return Booking.builder().start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();
    }

    public static BookingInfoDto toBookingInfoDto(Booking booking) {
        ItemDto itemDto = ItemDtoMapper.toItemDto(booking.getItem());
        UserDto userDto = UserDtoMapper.toUserDto(booking.getBooker());
        return BookingInfoDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(itemDto)
                .booker(userDto)
                .build();
    }

    public static List<BookingInfoDto> toBookingInfoDto(List<Booking> bookings) {
        return bookings.stream().map(BookingDtoMapper::toBookingInfoDto).collect(Collectors.toList());
    }
}