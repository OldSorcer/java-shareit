package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_IdOrderByStartDesc(Long userId);

    List<Booking> findAllByBooker_IdAndEndBeforeOrderByEndDesc(Long userId, LocalDateTime now);

    List<Booking> findAllByBooker_IdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findAllByBooker_IdAndStatusOrderByStartAsc(Long userId, BookingStatus status);

    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(Long userId);

    List<Booking> findAllByItem_Owner_IdAndEndBeforeOrderByEndDesc(Long userId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_IdAndStatusOrderByStartAsc(Long userId, BookingStatus status);

    Booking findFirstByItem_IdAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    Booking findFirstByItem_IdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime now);
}