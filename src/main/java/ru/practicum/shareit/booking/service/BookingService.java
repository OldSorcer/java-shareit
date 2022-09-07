package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    Booking createBooking(Booking booking, Long userId, Long itemId);
    Booking approveBooking(Long bookingId, Boolean approved, Long userId);
    Booking getBookingById(Long bookingId, Long userId);
    List<Booking> getBookings(BookingState state, Long userID);
    List<Booking> getBookingsByOwnerId(BookingState state, Long userId);
}
