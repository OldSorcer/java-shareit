package ru.practicum.shareit.booking.validator;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

public class BookingValidator {
    public static void validateBooking(BookingDto booking) {
        LocalDateTime now = LocalDateTime.now();
        if (booking.getStart().isBefore(now)) {
            throw new IllegalArgumentException("Некорректное время начала бронирования");
        }
        if (booking.getEnd().isBefore(now)) {
            throw new IllegalArgumentException("Некорректное время начала бронирования");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new IllegalArgumentException("Некорректное время начала бронирования");
        }
    }
}
