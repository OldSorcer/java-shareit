package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId, Pageable page);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByEndDesc(Long userId, LocalDateTime now, Pageable page);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now, Pageable page);

    List<Booking> findAllByBookerIdAndStatusOrderByStartAsc(Long userId, BookingStatus status, Pageable page);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId, Pageable page);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(Long userId, LocalDateTime now, Pageable page);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now, Pageable page);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartAsc(Long userId, BookingStatus status, Pageable page);

    Booking findFirstByItemIdAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Booking findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime now);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfter(Long itemId, LocalDateTime start, LocalDateTime end, Pageable page);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(Long itemOwnerId,
                                                                LocalDateTime start,
                                                                LocalDateTime end,
                                                                Pageable page);
}