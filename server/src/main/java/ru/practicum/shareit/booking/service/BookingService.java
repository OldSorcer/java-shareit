package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    BookingInfoDto createBooking(Booking booking, Long userId, Long itemId);

    BookingInfoDto approveBooking(Long bookingId, Boolean approved, Long userId);

    BookingInfoDto getBookingById(Long bookingId, Long userId);

    List<BookingInfoDto> getBookings(BookingState state, Long userID, int from, int size);

    List<BookingInfoDto> getBookingsByOwnerId(BookingState state, Long userId, int from, int size);
}
