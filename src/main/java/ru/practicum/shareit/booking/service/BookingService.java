package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Booking booking, Long userId, Long itemId);

    BookingDto approveBooking(Long bookingId, Boolean approved, Long userId);

    BookingDto getBookingById(Long bookingId, Long userId);

    List<BookingDto> getBookings(BookingState state, Long userID);

    List<BookingDto> getBookingsByOwnerId(BookingState state, Long userId);
}
