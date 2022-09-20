package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemDtoMapperTest {
    private static Item item;
    private static User user;
    private static Booking lastBooking;
    private static Booking nextBooking;
    private static ItemInfoDto.Booking lastBookingDto;
    private static ItemInfoDto.Booking nextBookingDto;
    private static ItemRequest itemRequest;
    private static ItemInfoDto itemInfoDto;
    private static ItemDto itemDto;
    private static Comment comment;

    @BeforeAll
    static void beforeAll() {
        user = new User(1L, "User1", "user1@email.ru");
        itemRequest = new ItemRequest(1L, "Description", user, LocalDateTime.now());
        item = Item.builder().id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .owner(user)
                .request(itemRequest).build();
        lastBooking = Booking.builder().item(item)
                .id(1L)
                .start(LocalDateTime.now().plusDays(4L))
                .end(LocalDateTime.now().plusDays(8L))
                .booker(user)
                .item(item)
                .status(BookingStatus.WAITING).build();
        nextBooking = lastBooking;
        lastBookingDto = ItemInfoDto.Booking.builder().id(lastBooking.getId())
                .start(lastBooking.getStart())
                .end(lastBooking.getEnd())
                .bookerId(lastBooking.getBooker().getId())
                .build();
        nextBookingDto = ItemInfoDto.Booking.builder().id(nextBooking.getId())
                .start(nextBooking.getStart())
                .end(nextBooking.getEnd())
                .bookerId(nextBooking.getBooker().getId())
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .ownerId(1L)
                .requestId(1L)
                .build();
        comment = Comment.builder()
                .id(1L)
                .text("Text")
                .author(user)
                .item(item)
                .created(LocalDateTime.now())
                .build();
        itemInfoDto = ItemInfoDto.builder().id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBookingDto)
                .nextBooking(nextBookingDto)
                .comments(CommentDtoMapper.toCommentDto(List.of(comment)))
                .requestId(1L)
                .build();
    }

    @Test
    void shouldReturnItemDto() {
        ItemDto result = ItemDtoMapper.toItemDto(item);
        assertEquals(itemDto, result);
    }

    @Test
    void shouldReturnItem() {
        Item result = ItemDtoMapper.toItem(itemDto);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
    }

    @Test
    void shouldReturnListOfItemDto() {
        List<ItemDto> expected = List.of(itemDto);
        List<ItemDto> result = ItemDtoMapper.toItemDto(List.of(item));
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnItemInfoDto() {
        ItemInfoDto result = ItemDtoMapper.toItemInfoDto(item, lastBooking, nextBooking, List.of(comment));
        assertEquals(itemInfoDto, result);
    }
}