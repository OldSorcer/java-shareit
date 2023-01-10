package ru.practicum.shareit.requests.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

public final class ItemRequestDtoMapper {
    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        return itemRequest;
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder().id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requesterId(itemRequest.getRequester().getId())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<Item> items) {
        ItemRequestDto itemRequestDto = toItemRequestDto(itemRequest);
        itemRequestDto.setItems(items.stream().map(ItemDtoMapper::toItemDto).collect(Collectors.toList()));
        return itemRequestDto;
    }
}
