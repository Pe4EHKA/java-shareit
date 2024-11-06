package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDtoJsonTest {
    private final JacksonTester<ItemDto> json;

    @Test
    @DisplayName("Сериализация ItemDto")
    void shouldSerialize() throws Exception {
        UserDto owner = new UserDto();
        owner.setId(1L);
        owner.setName("name");
        owner.setEmail("name@email.ru");

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Item Name");
        itemDto.setDescription("Item description");
        itemDto.setAvailable(true);
        itemDto.setOwner(owner);
        itemDto.setRequestId(100L);

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("Item Name");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Item description");
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.owner.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.owner.name")
                .isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.owner.email")
                .isEqualTo("name@email.ru");
        assertThat(result).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(100);
    }

    @Test
    @DisplayName("Десериализация ItemDto")
    void shouldDeserialize() throws Exception {
        String content = "{ \"id\": 1, " +
                "\"name\": \"Item Name\", " +
                "\"description\": \"Item description\", " +
                "\"available\": true, " +
                "\"owner\": { " +
                "\"id\": 1, " +
                "\"name\": \"name\", " +
                "\"email\": \"name@email.ru\" " +
                "}, " +
                "\"requestId\": 100 }";

        ItemDto itemDto = json.parseObject(content);

        assertThat(itemDto.getId())
                .isEqualTo(1L);
        assertThat(itemDto.getName())
                .isEqualTo("Item Name");
        assertThat(itemDto.getDescription())
                .isEqualTo("Item description");
        assertThat(itemDto.getAvailable())
                .isTrue();
        assertThat(itemDto.getOwner().getId())
                .isEqualTo(1L);
        assertThat(itemDto.getOwner().getName())
                .isEqualTo("name");
        assertThat(itemDto.getOwner().getEmail())
                .isEqualTo("name@email.ru");
        assertThat(itemDto.getRequestId())
                .isEqualTo(100L);
    }
}