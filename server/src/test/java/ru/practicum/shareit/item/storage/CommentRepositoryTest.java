package ru.practicum.shareit.item.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class CommentRepositoryTest {
    private User user = new User(null, "Name", "email");
    private Item item = Item.builder().name("Item").description("Description").available(true).owner(user).build();
    private Comment comment;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(user);
        item = itemRepository.save(item);
        comment = new Comment(null, "Comment", null, item, LocalDateTime.now());
        comment = commentRepository.save(comment);
    }

    @AfterEach
    void afterEach() {
        commentRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void shouldFindCommentByItemId() {
        List<Comment> result = commentRepository.findAllByItemIdOrderById(item.getId());
        assertEquals(1, result.size());
        assertFalse(result.isEmpty());
        assertEquals(comment.getId(), result.get(0).getId());
        assertEquals(item.getId(), result.get(0).getItem().getId());
    }
}