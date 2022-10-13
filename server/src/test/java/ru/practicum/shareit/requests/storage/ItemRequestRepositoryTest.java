package ru.practicum.shareit.requests.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class ItemRequestRepositoryTest {

    private User user1 = new User(null, "User1", "user1@email.ru");
    private User user2 = new User(null, "User2", "user2@email.ru");
    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @BeforeEach
    void beforeEach() {
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        itemRequest1 = new ItemRequest(null, "Description", user1, LocalDateTime.now());
        itemRequest2 = new ItemRequest(null, "Description", user2, LocalDateTime.now());
        itemRequest1 = itemRequestRepository.save(itemRequest1);
        itemRequest2 = itemRequestRepository.save(itemRequest2);
    }

    @AfterEach
    void afterEach() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldFindRequestByRequesterId() {
        List<ItemRequest> result = itemRequestRepository.findAllByRequesterIdOrderById(user1.getId());
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertEquals(user1.getName(), result.get(0).getRequester().getName());
    }

    @Test
    void shouldFindRequestsWithoutRequestsWithUserId() {
        List<ItemRequest> result = itemRequestRepository.findByRequesterIdNot(user1.getId(), Pageable.unpaged());
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertEquals(user2.getName(), result.get(0).getRequester().getName());
    }
}