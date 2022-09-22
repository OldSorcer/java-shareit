package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    private final static String HEADER = "X-Sharer-User-Id";
    private final User user = new User(1L, "User", "user@email.ru");
    private final Item item = new Item(1L, "Item", "Description", true, user, null);
    private final Booking booking = new Booking(1L, LocalDateTime.now().withNano(0), LocalDateTime.now().withNano(0), item, user, BookingStatus.WAITING);
    private final BookingDto bookingDto = BookingDtoMapper.toBookingDto(booking);
    private final BookingDto bookingEntryDto = new BookingDto();
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;

    @Test
    void shouldReturn200WhenBookingIsCreated() throws Exception {
        bookingEntryDto.setStart(LocalDateTime.now().withNano(0));
        bookingEntryDto.setEnd(LocalDateTime.MAX.withNano(0));
        bookingEntryDto.setItemId(item.getId());
        when(bookingService.createBooking(any(), anyLong(), anyLong()))
                .thenReturn(bookingEntryDto);
        mvc.perform(post("/bookings")
                        .header(HEADER, 1)
                        .content(mapper.writeValueAsString(bookingEntryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingEntryDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(String.format("%s", bookingEntryDto.getStart()))))
                .andExpect(jsonPath("$.end", is(String.format("%s", bookingEntryDto.getEnd()))));
    }

    @Test
    void shouldReturn200IfBookingStatusIsApproved() throws Exception {
        when(bookingService.approveBooking(anyLong(), any(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(String.format("%s", bookingDto.getStart()))))
                .andExpect(jsonPath("$.end", is(String.format("%s", bookingDto.getEnd()))))
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class));

    }

    @Test
    void shouldReturn200OnGetBookingByIdWhenIdIsValid() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(String.format("%s", bookingDto.getStart()))))
                .andExpect(jsonPath("$.end", is(String.format("%s", bookingDto.getEnd()))))
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class));
    }

    @Test
    void shouldReturn200WhenBookingGetAll() throws Exception {
        when(bookingService.getBookings(any(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(String.format("%s", bookingDto.getStart()))))
                .andExpect(jsonPath("$[0].end", is(String.format("%s", bookingDto.getEnd()))))
                .andExpect(jsonPath("$[0].item.id", is(item.getId()), Long.class));
    }

    @Test
    void shouldReturn200WhenBookingGetByOwnerId() throws Exception {
        when(bookingService.getBookingsByOwnerId(any(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(String.format("%s", bookingDto.getStart()))))
                .andExpect(jsonPath("$[0].end", is(String.format("%s", bookingDto.getEnd()))))
                .andExpect(jsonPath("$[0].item.id", is(item.getId()), Long.class));
    }
}