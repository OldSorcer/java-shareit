package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public final class ItemDtoMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getOwner())
                .available(item.getAvailable())
                .request(item.getRequest())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .owner(itemDto.getOwner())
                .available(itemDto.getAvailable())
                .request(itemDto.getRequest())
                .build();
    }

    public static List<ItemDto> toItemDto(List<Item> items) {
        return items.stream().map(ItemDtoMapper::toItemDto).collect(Collectors.toList());
    }

    public static ItemInfoDto toItemInfoDto(Item item, List<Booking> bookings) {
        Booking lastBooking = bookings.get(0);
        Booking nextBooking = bookings.get(1);
        ItemInfoDto.Booking lastBookingDto = ItemInfoDto.Booking.builder().id(lastBooking.getId())
                .bookerId(lastBooking.getBooker().getId())
                .start(lastBooking.getStart())
                .end(lastBooking.getEnd())
                .build();
        ItemInfoDto.Booking nextBookingDto = ItemInfoDto.Booking.builder().id(nextBooking.getId())
                .bookerId(nextBooking.getBooker().getId())
                .start(nextBooking.getStart())
                .end(nextBooking.getEnd())
                .build();
        return ItemInfoDto.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBookingDto)
                .nextBooking(nextBookingDto)
                .build();
    }
}
