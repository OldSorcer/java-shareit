package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {
    private final User user = new User(1L, "User", "user@email.ru");
    private final Item item = Item.builder().id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .owner(user).build();
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.save(user);
        itemRepository.save(item);
    }

    @AfterEach
    void afterEach() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldFindItemByOwnerId() {
        List<Item> result = itemRepository.findByOwnerIdOrderById(user.getId(), Pageable.unpaged());
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertEquals(user.getName(), result.get(0).getOwner().getName());
        assertEquals(item.getName(), result.get(0).getName());
    }

    @Test
    void shouldFindItemByName() {
        List<Item> result = itemRepository.searchBy("item", Pageable.unpaged());
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertEquals(item.getName(), result.get(0).getName());
    }

    @Test
    void shouldFindItemByDescription() {
        List<Item> result = itemRepository.searchBy("description", Pageable.unpaged());
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertEquals(item.getDescription(), result.get(0).getDescription());
    }
}