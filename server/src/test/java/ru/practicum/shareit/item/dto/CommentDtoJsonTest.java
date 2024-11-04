package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentDtoJsonTest {

    private final JacksonTester<CommentDto> json;

    @Test
    @DisplayName("Сериализация CommentDto")
    void shouldSerialize() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");
        commentDto.setAuthorName("Name");
        commentDto.setCreated(LocalDateTime.of(2024, 1, 1, 12, 0));

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text")
                .isEqualTo("Great item!");
        assertThat(result).extractingJsonPathStringValue("$.authorName")
                .isEqualTo("Name");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2024-01-01T12:00:00");
    }

    @Test
    @DisplayName("Десериализация CommentDto")
    void shouldDeserialize() throws Exception {
        String content = "{ \"id\": 1, " +
                "\"text\": \"Great item!\", " +
                "\"authorName\": \"Name\", " +
                "\"created\": \"2024-01-01T12:00:00\" }";

        CommentDto commentDto = json.parseObject(content);

        assertThat(commentDto.getId())
                .isEqualTo(1L);
        assertThat(commentDto.getText())
                .isEqualTo("Great item!");
        assertThat(commentDto.getAuthorName())
                .isEqualTo("Name");
        assertThat(commentDto.getCreated())
                .isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0));
    }
}