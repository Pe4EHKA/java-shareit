package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.Headers;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private BookingCreateDto bookingCreateDto;

    private User owner;

    private User booker;

    private Item item;

    private LocalDateTime now;

    private LocalDateTime nowPlus1Day;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        nowPlus1Day = now.plusDays(1);

        owner = new User();
        owner.setId(1L);
        owner.setName("Owner");
        owner.setEmail("owner@email.com");

        booker = new User();
        booker.setId(2L);
        booker.setEmail("booker@mail.ru");
        booker.setName("Booker");

        item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setOwner(owner);
        item.setAvailable(true);

        bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(item.getId());
        bookingCreateDto.setStart(now);
        bookingCreateDto.setEnd(nowPlus1Day);
    }

    @Test
    @DisplayName("Создание Booking и возврат BookingDto")
    void shouldAddBooking() throws Exception {
        BookingDto bookingDtoResponse = getBookingDtoResponse(BookingStatus.WAITING);

        Mockito.when(bookingService.addBooking(Mockito.any(), Mockito.any())).thenReturn(bookingDtoResponse);

        String result = mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .header(Headers.SHARER_USER_ID, booker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingCreateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoResponse), result);
        Mockito.verify(bookingService, Mockito.times(1))
                .addBooking(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Одобрение/Отклонение бронирования владельцем вещи")
    void shouldAnswerBooking() throws Exception {
        BookingDto bookingDtoResponse = getBookingDtoResponse(BookingStatus.APPROVED);

        Mockito.when(bookingService.approveBooking(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(bookingDtoResponse);

        String result = mockMvc.perform(MockMvcRequestBuilders.patch("/bookings/1")
                        .param("approved", "true")
                        .header(Headers.SHARER_USER_ID, booker.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoResponse), result);
        Mockito.verify(bookingService, Mockito.times(1))
                .approveBooking(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Получение информации о бронировании")
    void shouldGetBooking() throws Exception {
        BookingDto bookingDtoResponse = getBookingDtoResponse(BookingStatus.WAITING);

        Mockito.when(bookingService.getBooking(Mockito.any(), Mockito.any())).thenReturn(bookingDtoResponse);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/1")
                        .header(Headers.SHARER_USER_ID, booker.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoResponse), result);
        Mockito.verify(bookingService, Mockito.times(1))
                .getBooking(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Получение списка бронирований пользователя")
    void shouldGetUserBookings() throws Exception {
        List<BookingSearchState> states = List.of(BookingSearchState.values());

        for (BookingSearchState state : states) {
            Mockito.when(bookingService.getUserBookings(owner.getId(), state))
                    .thenReturn(Collections.emptyList());

            mockMvc.perform(MockMvcRequestBuilders.get("/bookings")
                            .header(Headers.SHARER_USER_ID, owner.getId())
                            .param("state", state.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json("[]"));

            Mockito.verify(bookingService, Mockito.times(1))
                    .getUserBookings(owner.getId(), state);
        }
    }

    @Test
    @DisplayName("Получение списка всех бронирований у владельца его вещей")
    void shouldGetOwnerBookings() throws Exception {
        List<BookingSearchState> states = List.of(BookingSearchState.values());

        for (BookingSearchState state : states) {
            Mockito.when(bookingService.getOwnerBookings(Mockito.any(), Mockito.any()))
                    .thenReturn(Collections.emptyList());

            mockMvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
                            .header(Headers.SHARER_USER_ID, owner.getId())
                            .param("state", state.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json("[]"));

            Mockito.verify(bookingService, Mockito.times(1)).getOwnerBookings(owner.getId(), state);
        }

    }

    private BookingDto getBookingDtoResponse(BookingStatus status) {
        BookingDto bookingDtoResponse = new BookingDto();
        bookingDtoResponse.setId(1L);
        bookingDtoResponse.setStart(now);
        bookingDtoResponse.setEnd(nowPlus1Day);
        bookingDtoResponse.setBooker(UserMapper.toUserDto(booker));
        bookingDtoResponse.setItem(ItemMapper.toItemDto(item));
        bookingDtoResponse.setStatus(status);

        return bookingDtoResponse;
    }
}