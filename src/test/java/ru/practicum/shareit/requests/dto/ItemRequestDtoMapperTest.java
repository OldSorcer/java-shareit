package ru.practicum.shareit.requests.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestDtoMapperTest {
    private static Item item;
    private static User user;
    private static ItemRequest itemRequest;
    private static ItemRequestDto itemRequestDto;

    @BeforeAll
    static void beforeAll() {
        user = new User(1L, "User", "user@email.ru");
        itemRequest = new ItemRequest(1L, "Text", user, LocalDateTime.of(2011, 11, 11, 1, 1));
        item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .owner(user)
                .request(itemRequest)
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Text")
                .requesterId(1L)
                .created(LocalDateTime.of(2011, 11, 11, 1, 1))
                .build();
     }

     @Test
    void shouldReturnItemRequestDto() {
        ItemRequestDto result = ItemRequestDtoMapper.toItemRequestDto(itemRequest);
        assertEquals(itemRequestDto, result);
     }

     @Test
    void shouldReturnItemRequest() {
        ItemRequest result = ItemRequestDtoMapper.toItemRequest(itemRequestDto);
        assertEquals("Text", result.getDescription());
     }

     @Test
    void shouldReturnItemRequestDtoWithItemList() {
        ItemRequestDto result = ItemRequestDtoMapper.toItemRequestDto(itemRequest, List.of(item));
        itemRequestDto.setItems(ItemDtoMapper.toItemDto(List.of(item)));
        assertEquals(itemRequestDto, result);
     }
}