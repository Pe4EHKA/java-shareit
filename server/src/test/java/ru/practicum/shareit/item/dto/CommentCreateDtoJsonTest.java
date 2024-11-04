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
class CommentCreateDtoJsonTest {

    private final JacksonTester<CommentCreateDto> json;

    @Test
    @DisplayName("Сериализация CommentCreateDto")
    void shouldSerialize() throws Exception {
        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("text");

        JsonContent<CommentCreateDto> content = json.write(commentCreateDto);

        assertThat(content).extractingJsonPathStringValue("$.text").isEqualTo("text");
    }

    @Test
    @DisplayName("Десериализация CommecntCreateDto")
    void shouldDeserialize() throws Exception {
        String content = "{\"text\":\"text\"}";

        CommentCreateDto commentCreateDto = json.parseObject(content);

        assertThat(commentCreateDto.getText()).isEqualTo("text");
    }

}