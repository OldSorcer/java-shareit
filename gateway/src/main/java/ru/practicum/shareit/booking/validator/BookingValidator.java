package ru.practicum.shareit.booking.validator;

import ru.practicum.shareit.booking.dto.BookingDto;

public class BookingValidator {
    public static void validateBooking(BookingDto booking) {
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new IllegalArgumentException("Некорректное время начала бронирования");
        }
        if (!booking.getStart().isBefore(booking.getEnd()) && !booking.getEnd().isAfter(booking.getStart())) {
            throw new IllegalArgumentException("Время начала и окончания бронирования не должно совпадать.");
        }
    }
}