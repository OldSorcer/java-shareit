package ru.practicum.shareit.requests.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRequestRepositoryTest {

    private final User user1 = new User(1L, "User1", "user1@email.ru");
    private final User user2 = new User(2L, "User2", "user2@email.ru");
    private final ItemRequest itemRequest1 = new ItemRequest(1L, "Description", user1, LocalDateTime.now());
    private final ItemRequest itemRequest2 = new ItemRequest(2L, "Description", user2, LocalDateTime.now());
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.save(user1);
        userRepository.save(user2);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);
    }

    @AfterEach
    void afterEach() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldFindRequestByRequesterId() {
        List<ItemRequest> result = itemRequestRepository.findAllByRequesterIdOrderById(1L);
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertEquals(user1.getName(), result.get(0).getRequester().getName());
    }

    @Test
    void shouldFindRequestsWithoutRequestsWithUserId() {
        List<ItemRequest> result = itemRequestRepository.findByRequesterIdNot(1L, Pageable.unpaged());
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertEquals(user2.getName(), result.get(0).getRequester().getName());
    }
}