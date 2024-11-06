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
class UserCreateDtoJsonTest {
    private final JacksonTester<UserCreateDto> json;

    @Test
    @DisplayName("Сериализация UserCreateDto")
    void shouldSerialize() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setId(1L);
        userCreateDto.setName("name");
        userCreateDto.setEmail("name@mail.ru");

        JsonContent<UserCreateDto> result = json.write(userCreateDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("name@mail.ru");
    }

    @Test
    @DisplayName("Десериализация UserCreateDto")
    void shouldDeserialize() throws Exception {
        String content = "{\"id\":\"1\",\"name\":\"name\",\"email\":\"name@mail.ru\"}";

        UserCreateDto userCreateDto = json.parseObject(content);

        assertThat(userCreateDto.getId()).isEqualTo(1L);
        assertThat(userCreateDto.getName()).isEqualTo("name");
        assertThat(userCreateDto.getEmail()).isEqualTo("name@mail.ru");
    }
}