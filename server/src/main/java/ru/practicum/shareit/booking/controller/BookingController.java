package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody BookingDto bookingDto,
                                    @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingService.createBooking(BookingDtoMapper.toBooking(bookingDto), userId, bookingDto.getItemId());
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable Long bookingId,
                                     @RequestParam Boolean approved,
                                     @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingService.approveBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId,
                                     @RequestHeader(USER_ID_HEADER) Long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestParam(defaultValue = "ALL", required = false) String state,
                                        @RequestHeader(USER_ID_HEADER) Long userID,
                                        @RequestParam(defaultValue = "0", required = false) Integer from,
                                        @RequestParam(defaultValue = "10", required = false) Integer size) {
        return bookingService.getBookings(BookingState.valueOf(state), userID, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerId(@RequestParam(defaultValue = "ALL", required = false) BookingState state,
                                                 @RequestHeader(USER_ID_HEADER) Long userId,
                                                 @RequestParam(defaultValue = "0", required = false) Integer from,
                                                 @RequestParam(defaultValue = "10", required = false) Integer size) {
        return bookingService.getBookingsByOwnerId(state, userId, from, size);
    }
}