package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemInfoDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Booking lastBooking;
    private Booking nextBooking;
    private List<CommentDto> comments;

    @Data
    @Builder
    public static class Booking {
        private Long id;
        private Long bookerId;
        private LocalDateTime start;
        private LocalDateTime end;
    }
}
