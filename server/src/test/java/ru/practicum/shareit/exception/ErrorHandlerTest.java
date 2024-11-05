package ru.practicum.shareit.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class, BookingController.class})
class ErrorHandlerTest {

    @MockBean
    UserService userService;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("NotFoundWhenUserNotFoundExceptionThrown")
    void shouldReturnNotFoundWhenUserNotFoundExceptionThrown() throws Exception {
        doThrow(new NotFoundException("User not found")).when(userService).getUserById(1L);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    @DisplayName("BadRequestWhenNotAvailableExceptionThrown")
    void shouldReturnBadRequestWhenNotAvailableExceptionThrown() throws Exception {
        doThrow(new NotAvailableException("Resource not available")).when(userService).getAllUsers();

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Resource not available"));
    }

    @Test
    @DisplayName("ConflictWhenIllegalArgumentExceptionThrown")
    void shouldReturnConflictWhenIllegalArgumentExceptionThrown() throws Exception {
        doThrow(new IllegalArgumentException("Illegal argument provided")).when(userService).getAllUsers();

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Illegal argument provided"));
    }
}