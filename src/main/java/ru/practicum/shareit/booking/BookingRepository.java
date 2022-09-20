package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByEndDesc(Long userId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStatusOrderByStartAsc(Long userId, BookingStatus status);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(Long userId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartAsc(Long userId, BookingStatus status);

    Booking findFirstByItemIdAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Booking findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime now);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long itemId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long itemOwnerId,
                                                                LocalDateTime start,
                                                                LocalDateTime end);
}