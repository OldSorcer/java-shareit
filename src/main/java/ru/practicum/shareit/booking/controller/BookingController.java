package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * // TODO .
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto createBooking(@RequestBody @Valid BookingDto bookingDto,
                                    @RequestHeader(USER_ID_HEADER) Long userId) {
        Booking createdBooking = bookingService.createBooking(BookingDtoMapper.toBooking(bookingDto), userId, bookingDto.getItemId());
        return BookingDtoMapper.toBookingDto(createdBooking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable Long bookingId,
                                     @RequestParam Boolean approved,
                                     @RequestHeader(USER_ID_HEADER) Long userId) {
        return BookingDtoMapper.toBookingDto(bookingService.approveBooking(bookingId, approved, userId));
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId,
                                     @RequestHeader(USER_ID_HEADER) Long userId) {
        return BookingDtoMapper.toBookingDto(bookingService.getBookingById(bookingId, userId));
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestParam(defaultValue = "ALL", required = false) BookingState state,
                                        @RequestHeader(USER_ID_HEADER) Long userID) {
        return BookingDtoMapper.toBookingDto(bookingService.getBookings(state, userID));
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerId(@RequestParam(defaultValue = "ALL", required = false) BookingState state,
                                                 @RequestHeader(USER_ID_HEADER) Long userId) {
        return BookingDtoMapper.toBookingDto(bookingService.getBookingsByOwnerId(state, userId));
    }
}