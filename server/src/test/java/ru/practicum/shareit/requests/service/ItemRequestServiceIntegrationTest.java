package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceIntegrationTest {
    @Autowired
    private final ItemRequestService itemRequestService;
    @Autowired
    private final UserService userService;
    @Autowired
    private final ItemService itemService;
    private final User user = new User(null, "User", "user@email.com");
    private final User requester = new User(null, "Requester", "requester@email.com");
    private final ItemRequest itemRequest = new ItemRequest(null, "Description", null, null);
    private final Item item = new Item(null, "Item", "Description", true, null, null);

    @Test
    void createItemRequest() {
        User createdUser = userService.createUser(user);
        User createdRequester = userService.createUser(requester);
        ItemRequestDto createdItemRequest = itemRequestService.createRequest(itemRequest, requester.getId());
        item.setRequest(new ItemRequest(createdItemRequest.getId(), "Description", createdRequester, null));
        Item createdItem = itemService.createItem(item, createdUser.getId());
        ItemRequestDto result = itemRequestService.findRequestById(requester.getId(), createdItemRequest.getId());
        assertEquals(createdItemRequest.getId(), result.getId());
        assertEquals(createdItemRequest.getDescription(), result.getDescription());
    }

    @Test
    void findAllRequests() {
        User createdUser = userService.createUser(user);
        User createdRequester = userService.createUser(requester);
        ItemRequestDto createdItemRequest = itemRequestService.createRequest(itemRequest, requester.getId());
        item.setRequest(new ItemRequest(1L, "Description", createdRequester, null));
        Item createdItem = itemService.createItem(item, createdUser.getId());
        List<ItemRequestDto> result = itemRequestService.findAllRequests(createdUser.getId(), 0, 10);
        assertEquals(1, result.size());
        assertEquals(createdItemRequest.getId(), result.get(0).getId());
    }
}