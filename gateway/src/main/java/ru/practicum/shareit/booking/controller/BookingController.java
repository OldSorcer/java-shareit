package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody @Valid BookingDto bookingDto,
                                        @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingClient.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable Long bookingId,
                                     @RequestParam Boolean approved,
                                     @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingClient.approve(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable Long bookingId,
                                     @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestParam(defaultValue = "ALL", required = false) BookingState state,
                                        @RequestHeader(USER_ID_HEADER) Long userId,
                                        @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                        @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return bookingClient.getBookings(state, userId, from, size);
    }

    @GetMapping("/owner")
    @Valid
    public ResponseEntity<Object> getBookingsByOwnerId(@RequestParam(defaultValue = "ALL", required = false) BookingState state,
                                                 @RequestHeader(USER_ID_HEADER) Long userId,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                                 @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return bookingClient.getBookingsByOwnerId(state, userId, from, size);
    }
}