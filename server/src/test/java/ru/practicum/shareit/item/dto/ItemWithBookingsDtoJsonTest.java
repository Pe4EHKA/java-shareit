package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemWithBookingsDtoJsonTest {

    private final JacksonTester<ItemWithBookingsDto> json;

    @Test
    @DisplayName("Сериализация ItemWithBookings")
    void shouldSerialize() throws Exception {
        UserDto owner = new UserDto();
        owner.setId(1L);
        owner.setName("name");
        owner.setEmail("name@mail.ru");

        ItemWithBookingsDto item = new ItemWithBookingsDto();
        item.setId(1L);
        item.setName("ItemName");
        item.setDescription("Item description");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setLastBooking(LocalDateTime.of(2024, 1, 1, 12, 0));
        item.setNextBooking(LocalDateTime.of(2024, 1, 2, 12, 0));
        item.setComments(Collections.emptyList());

        JsonContent<ItemWithBookingsDto> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("ItemName");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Item description");
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.owner.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.owner.name")
                .isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.owner.email")
                .isEqualTo("name@mail.ru");
        assertThat(result).extractingJsonPathStringValue("$.lastBooking")
                .isEqualTo("2024-01-01T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.nextBooking")
                .isEqualTo("2024-01-02T12:00:00");
        assertThat(result).extractingJsonPathArrayValue("$.comments")
                .isEmpty();
    }

    @Test
    @DisplayName("Десериализация ItemWithBookings")
    void shouldDeserialize() throws Exception {
        String content = "{ \"id\": 1, " +
                "\"name\": \"ItemName\", " +
                "\"description\": \"Item description\", " +
                "\"available\": true, " +
                "\"owner\": { " +
                "\"id\": 1, " +
                "\"name\": \"name\", " +
                "\"email\": \"name@mail.ru\" " +
                "}, " +
                "\"lastBooking\": \"2024-01-01T12:00:00\", " +
                "\"nextBooking\": \"2024-01-02T12:00:00\", " +
                "\"comments\": [] }";

        ItemWithBookingsDto item = json.parseObject(content);

        assertThat(item.getId())
                .isEqualTo(1L);
        assertThat(item.getName())
                .isEqualTo("ItemName");
        assertThat(item.getDescription())
                .isEqualTo("Item description");
        assertThat(item.getAvailable())
                .isTrue();
        assertThat(item.getOwner().getId())
                .isEqualTo(1L);
        assertThat(item.getOwner().getName())
                .isEqualTo("name");
        assertThat(item.getOwner().getEmail())
                .isEqualTo("name@mail.ru");
        assertThat(item.getLastBooking())
                .isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0));
        assertThat(item.getNextBooking())
                .isEqualTo(LocalDateTime.of(2024, 1, 2, 12, 0));
        assertThat(item.getComments())
                .isEmpty();
    }
}