package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private BookingDto.Item item;
    private BookingDto.User booker;
    private BookingStatus status;

    @Data
    @AllArgsConstructor
    public static class Item {
        private Long id;
        private String name;

        public Item(ru.practicum.shareit.item.model.Item item) {
            this.id = item.getId();
            this.name = item.getName();
        }
    }

    @Data
    @AllArgsConstructor
    public static class User {
        private Long id;

        public User(ru.practicum.shareit.user.model.User user) {
            this.id = user.getId();
        }
    }
}