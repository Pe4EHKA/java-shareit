package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemUpdateDtoJsonTest {

    private final JacksonTester<ItemUpdateDto> json;

    @Test
    @DisplayName("Сериализация ItemUpdateDto")
    void shouldSerialize() throws Exception {
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setId(1L);
        itemUpdateDto.setName("Updated Item");
        itemUpdateDto.setDescription("Updated description");
        itemUpdateDto.setAvailable(true);

        JsonContent<ItemUpdateDto> result = json.write(itemUpdateDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("Updated Item");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Updated description");
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isTrue();
    }

    @Test
    @DisplayName("Десериализцаия ItemUpdateDto")
    void shouldDeserialize() throws Exception {
        String content = "{ \"id\": 1, " +
                "\"name\": \"Updated Item\", " +
                "\"description\": \"Updated description\", " +
                "\"available\": true }";

        ItemUpdateDto itemUpdateDto = json.parseObject(content);

        assertThat(itemUpdateDto.getId())
                .isEqualTo(1);
        assertThat(itemUpdateDto.getName())
                .isEqualTo("Updated Item");
        assertThat(itemUpdateDto.getDescription())
                .isEqualTo("Updated description");
        assertThat(itemUpdateDto.getAvailable())
                .isEqualTo(true);
    }
}