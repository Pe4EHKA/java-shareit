package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestDtoJsonTest {

    private final JacksonTester<RequestDto> json;

    @Test
    @DisplayName("Сериализация RequestDto")
    void shouldSerialize() throws Exception {
        RequestDto requestDto = getRequestDto();

        JsonContent<RequestDto> content = json.write(requestDto);

        assertThat(content).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("$.description")
                .isEqualTo("Request description");
        assertThat(content).extractingJsonPathStringValue("$.created")
                .isEqualTo("2024-01-01T00:00:00");
        assertThat(content).extractingJsonPathNumberValue("$.user.id")
                .isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("$.user.name")
                .isEqualTo("name");
        assertThat(content).extractingJsonPathStringValue("$.user.email")
                .isEqualTo("name@mail.ru");
        assertThat(content).extractingJsonPathArrayValue("$.items")
                .hasSize(1);
        assertThat(content).extractingJsonPathNumberValue("$.items[0].id")
                .isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("$.items[0].name")
                .isEqualTo("name");
        assertThat(content).extractingJsonPathStringValue("$.items[0].description")
                .isEqualTo("Item description");
        assertThat(content).extractingJsonPathBooleanValue("$.items[0].available")
                .isEqualTo(true);
        assertThat(content).extractingJsonPathNumberValue("$.items[0].owner.id")
                .isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("$.items[0].owner.name")
                .isEqualTo("name");
        assertThat(content).extractingJsonPathStringValue("$.items[0].owner.email")
                .isEqualTo("name@mail.ru");
    }

    private static RequestDto getRequestDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("name");
        userDto.setEmail("name@mail.ru");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("name");
        itemDto.setDescription("Item description");
        itemDto.setAvailable(true);
        itemDto.setOwner(userDto);

        RequestDto requestDto = new RequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Request description");
        requestDto.setCreated(LocalDateTime.of(2024, 1, 1, 0, 0, 0));
        requestDto.setUser(userDto);
        requestDto.setItems(List.of(itemDto));
        return requestDto;
    }

    @Test
    @DisplayName("Десериализация RequestDto")
    void shouldDeserialize() throws Exception {
        String content = "{ " +
                "\"id\": 1, " +
                "\"description\": \"Request description\", " +
                "\"created\": \"2024-01-01T00:00:00\", " +
                "\"user\": { " +
                "\"id\": 1, " +
                "\"name\": \"name\", " +
                "\"email\": \"name@mail.ru\" " +
                "}, " +
                "\"items\": [ " +
                "{ " +
                "\"id\": 1, " +
                "\"name\": \"name\", " +
                "\"description\": \"Item description\", " +
                "\"available\": true, " +
                "\"owner\": { " +
                "\"id\": 1, " +
                "\"name\": \"name\", " +
                "\"email\": \"name@mail.ru\" " +
                "} " +
                "} " +
                "] " +
                "}";


        RequestDto requestDto = json.parseObject(content);

        assertThat(requestDto.getId())
                .isEqualTo(1L);
        assertThat(requestDto.getDescription())
                .isEqualTo("Request description");
        assertThat(requestDto.getCreated())
                .isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0, 0));
        assertThat(requestDto.getUser().getId()).isEqualTo(1L);
        assertThat(requestDto.getUser().getName())
                .isEqualTo("name");
        assertThat(requestDto.getUser().getEmail())
                .isEqualTo("name@mail.ru");
        assertThat(requestDto.getItems())
                .hasSize(1);
        assertThat(requestDto.getItems().getFirst().getId())
                .isEqualTo(1L);
        assertThat(requestDto.getItems().getFirst().getName())
                .isEqualTo("name");
        assertThat(requestDto.getItems().getFirst().getDescription())
                .isEqualTo("Item description");
        assertThat(requestDto.getItems().getFirst().getAvailable())
                .isEqualTo(true);
        assertThat(requestDto.getItems().getFirst().getOwner().getId())
                .isEqualTo(1L);
        assertThat(requestDto.getItems().getFirst().getOwner().getName())
                .isEqualTo("name");
        assertThat(requestDto.getItems().getFirst().getOwner().getEmail())
                .isEqualTo("name@mail.ru");
    }
}