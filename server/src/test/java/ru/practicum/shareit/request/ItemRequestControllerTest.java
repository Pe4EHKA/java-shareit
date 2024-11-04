package ru.practicum.shareit.request;

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
import ru.practicum.shareit.item.Headers;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @MockBean
    private RequestService requestService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private User owner;

    private User booker;

    private Item item;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
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

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription(item.getDescription());
    }

    @Test
    @DisplayName("Добавить запрос")
    void shouldAddRequest() throws Exception {
        RequestDto requestDto = getRequestDto();

        Mockito.when(requestService.addRequest(Mockito.any(), Mockito.any()))
                .thenReturn(requestDto);

        String result = mockMvc.perform(MockMvcRequestBuilders.post("/requests")
                        .header(Headers.SHARER_USER_ID, booker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(requestDto), result);
        Mockito.verify(requestService, Mockito.times(1))
                .addRequest(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Получить запросы пользователя")
    void shouldGetUserRequests() throws Exception {
        List<RequestDto> requestDtoList = List.of(getRequestDto());

        Mockito.when(requestService.getUserRequests(Mockito.any()))
                .thenReturn(requestDtoList);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header(Headers.SHARER_USER_ID, booker.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(requestDtoList), result);
        Mockito.verify(requestService, Mockito.times(1)).getUserRequests(Mockito.any());
    }

    @Test
    @DisplayName("Получение всех запросов")
    void shouldGetAllRequests() throws Exception {
        List<RequestDto> requestDtoList = List.of(getRequestDto());

        Mockito.when(requestService.getAllRequests())
                .thenReturn(requestDtoList);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .header(Headers.SHARER_USER_ID, booker.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(requestDtoList), result);
        Mockito.verify(requestService, Mockito.times(1)).getAllRequests();
    }

    @Test
    @DisplayName("Получение запроса по id")
    void shouldGetRequest() throws Exception {
        RequestDto requestDto = getRequestDto();

        Mockito.when(requestService.getRequest(Mockito.any()))
                .thenReturn(requestDto);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/requests/" + requestDto.getId())
                        .header(Headers.SHARER_USER_ID, booker.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(requestDto), result);
        Mockito.verify(requestService, Mockito.times(1)).getRequest(Mockito.any());
    }

    private RequestDto getRequestDto() {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(1L);
        requestDto.setUser(UserMapper.toUserDto(booker));
        requestDto.setDescription(item.getDescription());
        requestDto.setCreated(LocalDateTime.now());
        requestDto.setItems(List.of(ItemMapper.toItemDto(item)));

        return requestDto;
    }
}