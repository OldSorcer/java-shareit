package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class ItemDtoMapper {
    public static ItemDto toItemDto(Item item) {
        Long requestId = null;
        if (Objects.nonNull(item.getRequest())) {
            requestId = item.getRequest().getId();
        }
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwner().getId())
                .available(item.getAvailable())
                .requestId(requestId)
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        ItemRequest itemRequest = null;
        if (Objects.nonNull(itemDto.getRequestId())) {
            itemRequest = new ItemRequest();
            itemRequest.setId(itemDto.getRequestId());
        }
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .request(itemRequest)
                .available(itemDto.getAvailable())
                .build();
    }

    public static List<ItemDto> toItemDto(List<Item> items) {
        return items.stream().map(ItemDtoMapper::toItemDto).collect(Collectors.toList());
    }

    public static ItemInfoDto toItemInfoDto(Item item, Booking lastBooking, Booking nextBooking, List<Comment> comments) {
        ItemInfoDto.Booking lastBookingDto = null;
        ItemInfoDto.Booking nextBookingDto = null;
        Long requestId = null;
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
        if (Objects.nonNull(item.getRequest())) {
            requestId = item.getRequest().getId();
        }
        return ItemInfoDto.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBookingDto)
                .nextBooking(nextBookingDto)
                .comments(CommentDtoMapper.toCommentDto(comments))
                .requestId(requestId)
                .build();
    }
}