package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    @NotNull(message = "Идентификатор предмета не может быть пустым")
    @Min(value = 1, message = "ID не может быть меньше 1")
    private Long itemId;
    @NotNull(message = "Время начала бронирования не может быть пустым")
    @FutureOrPresent(message = "Время начала бронирования должно начинаться с текущей или будущей даты")
    private LocalDateTime start;
    @NotNull(message = "Время окончания бронирования не может быть пустым")
    @Future(message = "Время окончания бронирования должно быть указано позднее текущей даты")
    private LocalDateTime end;
}