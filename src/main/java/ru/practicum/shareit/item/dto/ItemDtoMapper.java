package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public static List<ItemDto> toItemDtos(List<Item> items) {
        return items.stream().map(ItemDtoMapper::toItemDto).collect(Collectors.toList());
    }

    public static ItemInfoDto toItemInfoDto(Item item, Booking lastBooking, Booking nextBooking) {
        ItemInfoDto.Booking lastBookingDto = null;
        ItemInfoDto.Booking nextBookingDto = null;
        if (Objects.nonNull((lastBooking))) {
            lastBookingDto = ItemInfoDto.Booking.builder().id(lastBooking.getId())
                    .start(lastBooking.getStart())
                    .end(lastBooking.getEnd())
                    .bookerId(lastBooking.getBooker().getId())
                    .build();
        }
        if (Objects.nonNull(nextBooking)) {
            nextBookingDto = ItemInfoDto.Booking.builder().id(nextBooking.getId())
                    .start(nextBooking.getStart())
                    .end(nextBooking.getEnd())
                    .bookerId(nextBooking.getBooker().getId())
                    .build();
        }
        return ItemInfoDto.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBookingDto)
                .nextBooking(nextBookingDto)
                .build();
    }

    public static ItemInfoDto toItemInfoDto(Item item, BookingService service, Long userId) {
        ItemInfoDto.Booking lastBookingDto = null;
        ItemInfoDto.Booking nextBookingDto = null;
        if (userId.equals(item.getOwner().getId())) {
            Booking lastBooking = service.getLastBookingByItemId(item.getId());
            Booking nextBooking = service.getNextBookingByItemId(item.getId());
            if (Objects.nonNull(lastBooking)) {
                lastBookingDto = ItemInfoDto.Booking.builder().id(lastBooking.getId())
                        .bookerId(lastBooking.getBooker().getId())
                        .start(lastBooking.getStart())
                        .end(lastBooking.getEnd())
                        .build();
            }
            if (Objects.nonNull(nextBooking)) {
                nextBookingDto = ItemInfoDto.Booking.builder().id(nextBooking.getId())
                        .bookerId(nextBooking.getBooker().getId())
                        .start(nextBooking.getStart())
                        .end(nextBooking.getEnd())
                        .build();
            }
        }
        return ItemInfoDto.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBookingDto)
                .nextBooking(nextBookingDto)
                .build();
    }

    public static List<ItemInfoDto> toItemInfoDto(List<Item> items, BookingService service) {
        List<ItemInfoDto> itemInfoDtos = new ArrayList<>();
        Booking lastBooking;
        Booking nextBooking;
        for (Item item : items) {
            lastBooking = service.getLastBookingByItemId(item.getId());
            nextBooking = service.getNextBookingByItemId(item.getId());
            itemInfoDtos.add(ItemDtoMapper.toItemInfoDto(item, lastBooking, nextBooking));
        }
        return itemInfoDtos;
    }
}
