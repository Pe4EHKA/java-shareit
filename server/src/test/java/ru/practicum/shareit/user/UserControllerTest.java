package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private User owner;

    private User booker;

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
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void shouldGetAllUsers() throws Exception {
        List<UserDto> userDtos = List.of(UserMapper.toUserDto(owner));

        Mockito.when(userService.getAllUsers()).thenReturn(userDtos);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDtos), result);
        Mockito.verify(userService, Mockito.times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Получение пользователя по id")
    void shouldGetUserById() throws Exception {
        UserDto userDto = UserMapper.toUserDto(owner);

        Mockito.when(userService.getUserById(owner.getId())).thenReturn(userDto);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/users/" + owner.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
        Mockito.verify(userService, Mockito.times(1)).getUserById(owner.getId());
    }

    @Test
    @DisplayName("Добавление пользователя")
    void addUser() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName(owner.getName());
        userCreateDto.setEmail(owner.getEmail());

        UserDto userDto = UserMapper.toUserDto(owner);

        Mockito.when(userService.addUser(userCreateDto)).thenReturn(userDto);

        String result = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
        Mockito.verify(userService, Mockito.times(1)).addUser(userCreateDto);
    }

    @Test
    @DisplayName("Обновление пользователя")
    void updateUser() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(1L);
        userUpdateDto.setName(booker.getName());
        userUpdateDto.setEmail(booker.getEmail());

        UserDto userDto = UserMapper.toUserDto(owner);

        Mockito.when(userService.updateUser(userUpdateDto, userUpdateDto.getId()))
                .thenReturn(userDto);

        String result = mockMvc.perform(MockMvcRequestBuilders.patch("/users/" + owner.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(userDto), result);
        Mockito.verify(userService, Mockito.times(1))
                .updateUser(userUpdateDto, userUpdateDto.getId());
    }

    @Test
    @DisplayName("Удаление пользователя")
    void deleteUser() throws Exception {
        Long userId = owner.getId();

        Mockito.doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + userId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(userService, Mockito.times(1)).deleteUser(userId);
    }
}