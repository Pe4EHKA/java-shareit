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
class ItemCreateDtoJsonTest {

    private final JacksonTester<ItemCreateDto> json;

    @Test
    @DisplayName("Сериализация ItemCreateDto")
    void shouldSerialize() throws Exception {
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("New Item");
        itemCreateDto.setDescription("Description of the new item");
        itemCreateDto.setAvailable(true);
        itemCreateDto.setRequestId(123L);

        JsonContent<ItemCreateDto> result = json.write(itemCreateDto);

        assertThat(result).extractingJsonPathStringValue("$.name")
                .isEqualTo("New Item");
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Description of the new item");
        assertThat(result).extractingJsonPathBooleanValue("$.available")
                .isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(123);
    }

    @Test
    @DisplayName("Десериализация itemCreateDto")
    void shouldDeserialize() throws Exception {
        String content = "{ \"name\": \"New Item\", " +
                "\"description\": \"Description of the new item\", " +
                "\"available\": true, " +
                "\"requestId\": 123 }";

        ItemCreateDto itemCreateDto = json.parseObject(content);

        assertThat(itemCreateDto.getName())
                .isEqualTo("New Item");
        assertThat(itemCreateDto.getDescription())
                .isEqualTo("Description of the new item");
        assertThat(itemCreateDto.getAvailable())
                .isTrue();
        assertThat(itemCreateDto.getRequestId())
                .isEqualTo(123L);
    }
}