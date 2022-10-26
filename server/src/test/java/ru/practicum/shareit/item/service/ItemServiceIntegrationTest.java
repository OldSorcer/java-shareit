package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceIntegrationTest {
    @Autowired
    private final ItemService itemService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final BookingService bookingService;
    private final User user = new User(1L, "User", "user@email.com");
    private final User booker = new User(1L, "Booker", "booker@email.com");
    private final ItemRequest itemRequest = new ItemRequest(1L, "Description", user, LocalDateTime.now());
    private final Item updatedItem = new Item(1L, "ItemUpdated", "DescriptionUpdated", true, user, itemRequest);
    private final Booking booking = new Booking(1L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusDays(1), null, null, BookingStatus.WAITING);
    private final Item item = new Item(1L, "Item", "Description", true, null, null);

    @Test
    void updateItem() {
        User createdUser = userService.createUser(user);
        Item createdItem = itemService.createItem(item, createdUser.getId());
        Item result = itemService.updateItem(createdItem.getId(), updatedItem, createdUser.getId());
        assertEquals(createdItem.getId(), result.getId());
        assertEquals(updatedItem.getName(), result.getName());
        assertEquals(updatedItem.getDescription(), result.getDescription());
    }

    @Test
    void getItemByOwnerId() {
        User createdUser = userService.createUser(user);
        User createdBooker = userService.createUser(booker);
        Item createdItem = itemService.createItem(item, createdUser.getId());
        booking.setBooker(createdBooker);
        BookingInfoDto createdBooking = bookingService.createBooking(booking, createdBooker.getId(), createdItem.getId());
        List<ItemInfoDto> result = itemService.getItemByOwnerId(createdUser.getId(), 1, 10);
        assertEquals(1, result.size());
        assertEquals(createdItem.getId(), result.get(0).getId());
        assertEquals(createdItem.getName(), result.get(0).getName());
        assertEquals(createdItem.getDescription(), result.get(0).getDescription());
        assertEquals(createdBooking.getId(), result.get(0).getNextBooking().getId());
    }

    @Test
    void getItemById() {
        User createdUser = userService.createUser(user);
        Item createdItem = itemService.createItem(item, createdUser.getId());
        ItemInfoDto result = itemService.getItemById(createdItem.getId(), createdUser.getId());
        assertEquals(createdItem.getId(), result.getId());
        assertEquals(createdItem.getName(), result.getName());
        assertEquals(createdItem.getDescription(), result.getDescription());
    }

    @Test
    void searchByDescription() {
        User createdUser = userService.createUser(user);
        Item createdItem = itemService.createItem(item, createdUser.getId());
        List<Item> result = itemService.searchBy("Description", 0, 10);
        assertEquals(1, result.size());
        assertEquals(createdItem.getDescription(), result.get(0).getDescription());
    }
}