package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class ItemRepositoryTest {
    private User user = new User(null, "User", "user@email.ru");
    private Item item;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(user);
        item = Item.builder()
                .name("Item")
                .description("Description")
                .available(true)
                .owner(user).build();
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