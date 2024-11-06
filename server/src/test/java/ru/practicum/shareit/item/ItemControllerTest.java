package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private ItemCreateDto itemCreateDto;

    private ItemUpdateDto itemUpdateDto;

    private ItemWithBookingsDto itemWithBookingsDto;

    private CommentCreateDto commentCreateDto;

    private User owner;


    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setName("Owner");
        owner.setEmail("owner@email.com");

        itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("ItemCreate");
        itemCreateDto.setDescription("Description");
        itemCreateDto.setAvailable(true);

        itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setId(1L);
        itemUpdateDto.setName("ItemUpdate");
        itemUpdateDto.setDescription("Description");
        itemUpdateDto.setAvailable(true);

        itemWithBookingsDto = new ItemWithBookingsDto();
        itemWithBookingsDto.setId(2L);
        itemWithBookingsDto.setName("ItemWithBookings");
        itemWithBookingsDto.setDescription("Description");
        itemWithBookingsDto.setAvailable(true);
        itemWithBookingsDto.setOwner(UserMapper.toUserDto(owner));

        commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("text");
    }

    @Test
    @DisplayName("Создание Item")
    void shouldAddItem() throws Exception {
        ItemDto itemDto = getItemDto(itemCreateDto, owner);

        Mockito.when(itemService.addItem(Mockito.any(), Mockito.any()))
                .thenReturn(itemDto);

        String result = mockMvc.perform(MockMvcRequestBuilders.post("/items")
                        .header(Headers.SHARER_USER_ID, String.valueOf(owner.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemCreateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
        Mockito.verify(itemService, Mockito.times(1)).addItem(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Обновление Item")
    void shouldUpdateItem() throws Exception {
        ItemDto itemDto = getItemDto(itemUpdateDto, owner);

        Mockito.when(itemService.updateItem(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(itemDto);

        String result = mockMvc.perform(MockMvcRequestBuilders.patch("/items/" + itemDto.getId())
                        .header(Headers.SHARER_USER_ID, String.valueOf(owner.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemUpdateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
        Mockito.verify(itemService, Mockito.times(1))
                .updateItem(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Получение Item")
    void shouldGetItem() throws Exception {
        Mockito.when(itemService.getItemById(Mockito.any()))
                .thenReturn(itemWithBookingsDto);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/items/" + itemWithBookingsDto.getId())
                        .header(Headers.SHARER_USER_ID, String.valueOf(owner.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemWithBookingsDto), result);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(Mockito.any());
    }

    @Test
    @DisplayName("Получение вещей пользователя")
    void shouldGetUserItems() throws Exception {
        Collection<ItemWithBookingsDto> items = List.of(itemWithBookingsDto);

        Mockito.when(itemService.getItemsByOwnerId(Mockito.any()))
                .thenReturn(items);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header(Headers.SHARER_USER_ID, String.valueOf(owner.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(items), result);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemsByOwnerId(Mockito.any());
    }

    @Test
    @DisplayName("Получение вещей по тексту названия")
    void getItemsByText() throws Exception {
        Collection<ItemDto> items = List.of(getItemDto(itemUpdateDto, owner));

        Mockito.when(itemService.getItemsByText(Mockito.any()))
                .thenReturn(items);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/items/search")
                        .param("text", "text")
                        .header(Headers.SHARER_USER_ID, String.valueOf(owner.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(items), result);
        Mockito.verify(itemService, Mockito.times(1))
                .getItemsByText(Mockito.any());
    }

    @Test
    @DisplayName("Добавление комментария")
    void addComment() throws Exception {
        CommentDto commentDto = getCommentDto(commentCreateDto);

        Mockito.when(itemService.addComment(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(commentDto);

        String result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/items/" + itemWithBookingsDto.getId() + "/comment")
                        .header(Headers.SHARER_USER_ID, String.valueOf(owner.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentCreateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDto), result);
        Mockito.verify(itemService, Mockito.times(1))
                .addComment(Mockito.any(), Mockito.any(), Mockito.any());
    }

    private ItemDto getItemDto(ItemCreateDto itemCreateDto, User owner) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName(itemCreateDto.getName());
        itemDto.setDescription(itemCreateDto.getDescription());
        itemDto.setAvailable(itemCreateDto.getAvailable());
        itemDto.setOwner(UserMapper.toUserDto(owner));

        return itemDto;
    }

    private ItemDto getItemDto(ItemUpdateDto itemUpdateDto, User owner) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName(itemUpdateDto.getName());
        itemDto.setDescription(itemUpdateDto.getDescription());
        itemDto.setAvailable(itemUpdateDto.getAvailable());
        itemDto.setOwner(UserMapper.toUserDto(owner));

        return itemDto;
    }

    private CommentDto getCommentDto(CommentCreateDto commentCreateDto) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText(commentCreateDto.getText());

        return commentDto;
    }
}