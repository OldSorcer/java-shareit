package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    private static final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .ownerId(1L)
            .build();
    private static final Item item = ItemDtoMapper.toItem(itemDto);
    private static final String HEADER = "X-User-Sharer-Id";
    private final ItemInfoDto itemInfoDto = ItemInfoDto.builder()
            .id(1L)
            .name("Item")
            .description("Description")
            .available(true)
            .build();
    private final CommentDto commentDto = CommentDto.builder()
            .text("Comment")
            .id(1L)
            .authorName("Name")
            .build();
    private final Comment comment = Comment.builder()
            .id(1L)
            .text("Comment")
            .author(new User(1L, "Name", "user@email.com"))
            .item(item)
            .created(LocalDateTime.now())
            .build();
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mvc;

    @BeforeAll
    static void beforeAll() {
        item.setOwner(new User(1L, "Name", "user@email.com"));
    }

    @Test
    void shouldReturnStatus200IfItemCreated() throws Exception {
        when(itemService.createItem(any(), anyLong()))
                .thenReturn(item);
        mvc.perform(post("/items")
                        .header(HEADER, 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void shouldReturnStatus200IfItemUpdated() throws Exception {
        when(itemService.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(item);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void shouldReturnStatus200WhenGetItemByValidId() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(itemInfoDto);

        mvc.perform(get("/items/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemInfoDto.getName())))
                .andExpect(jsonPath("$.description", is(itemInfoDto.getDescription())));
    }

    @Test
    void shouldReturnStatus200WhenGetByValidOwnerId() throws Exception {
        when(itemService.getItemByOwnerId(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemInfoDto));

        mvc.perform(get("/items")
                        .header(HEADER, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(itemInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemInfoDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemInfoDto.getDescription())));
    }

    @Test
    void shouldReturnStatus200WhenSearchIsSuccessful() throws Exception {
        when(itemService.searchBy(any(), anyInt(), anyInt()))
                .thenReturn(List.of(item));

        mvc.perform(get("/items/search?text=Description")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())));
    }

    @Test
    void shouldReturnStatus200WhenCommentCreated() throws Exception {
        when(itemService.createComment(any(), anyLong(), anyLong()))
                .thenReturn(comment);

        mvc.perform(post("/items/1/comment")
                        .header(HEADER, 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }
}