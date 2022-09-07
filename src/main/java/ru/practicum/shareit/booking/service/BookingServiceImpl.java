package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.EntityCreateException;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking createBooking(Booking booking, Long userId, Long itemId) {
        booking.setStatus(BookingStatus.WAITING);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Вещи с ID %d не существует", itemId)));
        User user = findUser(userId);
        booking.setBooker(user);
        booking.setItem(item);
        checkBooking(booking);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approveBooking(Long bookingId, Boolean approved, Long userId) {
        Booking booking = findBooking(bookingId);
        findUser(userId);
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new EntityNotFoundException("Вы не можете подтверждать или отклонять бронирование вещи," +
                    " владельцем которой не являетесь");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new EntityCreateException("Бронирование уже подтверждено");
        } else if (booking.getStatus().equals(BookingStatus.REJECTED)) {
            throw new EntityCreateException("Бронирование уже отклонено");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long bookingId, Long userID) {
        Booking foundedBooking = findBooking(bookingId);
        findUser(userID);
        checkBookingAccess(foundedBooking, userID);
        return foundedBooking;
    }

    @Override
    public List<Booking> getBookings(BookingState state, Long userId) {
        findUser(userId);
        List<Booking> bookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case PAST:
                bookings = bookingRepository.findAllByBooker_IdAndEndBeforeOrderByEndDesc(userId, now);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBooker_IdAndStartAfterOrderByStartDesc(userId, now);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBooker_IdAndStatusOrderByStartAsc(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBooker_IdAndStatusOrderByStartAsc(userId, BookingStatus.REJECTED);
                break;
            case ALL:
                bookings = bookingRepository.findAllByBooker_IdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBooker_IdAndStartBeforeAndEndAfter(userId, now, now);
                break;
        }
        return bookings;
    }

    @Override
    public List<Booking> getBookingsByOwnerId(BookingState state, Long userId) {
        findUser(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case PAST:
                bookings = bookingRepository.findAllByItem_Owner_IdAndEndBeforeOrderByEndDesc(userId, now);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(userId, now);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartAsc(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartAsc(userId, BookingStatus.REJECTED);
                break;
            case ALL:
                bookings = bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStartBeforeAndEndAfter(userId, now, now);
                break;
        }
        return bookings;
    }

    private void checkBooking(Booking booking) {
        LocalDateTime now = LocalDateTime.now();
        if (!booking.getItem().getAvailable()) {
            throw new EntityCreateException(String.format("Вещь с ID %d недоступна", booking.getItem().getId()));
        }
        if (booking.getStart().isBefore(now)) {
            throw new EntityCreateException("Некорректное время начала бронирования");
        }
        if (booking.getEnd().isBefore(now)) {
            throw new EntityCreateException("Некорректное время окончания бронирования");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new EntityCreateException("Время окончания бронирования не может быть раньше времени начала бронирования");
        }
        if (booking.getItem().getOwner().getId().equals(booking.getBooker().getId())) {
            throw new EntityNotFoundException("Вы являетесь владельцем предмета и не можете создать бронирование");
        }
    }

    private void checkBookingAccess(Booking booking, Long userId) {
        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new EntityNotFoundException("Вы не можете проматривать или редактировать информацию о " +
                    "бронировании, так как не являетесь владельцем вещи или автором бронирования");
        }
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователя с ID %d не существует", id)));
    }

    private Booking findBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Бронирования с ID %d не существует",
                        bookingId)));
    }
}