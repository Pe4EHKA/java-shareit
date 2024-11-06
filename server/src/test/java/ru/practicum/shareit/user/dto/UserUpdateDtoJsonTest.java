package ru.practicum.shareit.user.dto;

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
class UserUpdateDtoJsonTest {
    private final JacksonTester<UserUpdateDto> json;

    @Test
    @DisplayName("Сериализация UserUpdateDto")
    void shouldSerialize() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(1L);
        userUpdateDto.setName("name");
        userUpdateDto.setEmail("name@mail.ru");

        JsonContent<UserUpdateDto> content = json.write(userUpdateDto);

        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(content).extractingJsonPathStringValue("$.email").isEqualTo("name@mail.ru");
    }

    @Test
    @DisplayName("Десериализация UserUpdateDto")
    void shouldDeserialize() throws Exception {
        String content = "{\"id\":\"1\",\"name\":\"name\",\"email\":\"name@mail.ru\"}";

        UserUpdateDto userUpdateDto = json.parseObject(content);

        assertThat(userUpdateDto.getId()).isEqualTo(1L);
        assertThat(userUpdateDto.getName()).isEqualTo("name");
        assertThat(userUpdateDto.getEmail()).isEqualTo("name@mail.ru");
    }
}