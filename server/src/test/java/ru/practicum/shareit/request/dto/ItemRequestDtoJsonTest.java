package ru.practicum.shareit.request.dto;

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
class ItemRequestDtoJsonTest {
    private final JacksonTester<ItemRequestDto> json;

    @Test
    @DisplayName("Сериализация ItemRequestDto")
    void testItemRequestDtoSerialization() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("This is a test description");

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("This is a test description");
    }

    @Test
    @DisplayName("Десериализация ItemRequestDto")
    void testItemRequestDtoDeserialization() throws Exception {
        String content = "{\"description\":\"This is a test description\"}";

        ItemRequestDto itemRequestDto = json.parseObject(content);

        assertThat(itemRequestDto.getDescription()).isEqualTo("This is a test description");
    }
}