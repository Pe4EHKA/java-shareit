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
class UserDtoJsonTest {
    private final JacksonTester<UserDto> json;

    @Test
    @DisplayName("Сериализация UserDto")
    void shouldSerialize() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("name");
        userDto.setEmail("name@mail.ru");

        JsonContent<UserDto> content = json.write(userDto);

        assertThat(content).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("$.name").isEqualTo("name");
        assertThat(content).extractingJsonPathStringValue("$.email").isEqualTo("name@mail.ru");
    }

    @Test
    @DisplayName("Десериализация UserDto")
    void shouldDeserialize() throws Exception {
        String content = "{\"id\":\"1\",\"name\":\"name\",\"email\":\"name@mail.ru\"}";

        UserDto userDto = json.parseObject(content);

        assertThat(userDto.getId()).isEqualTo(1L);
        assertThat(userDto.getName()).isEqualTo("name");
        assertThat(userDto.getEmail()).isEqualTo("name@mail.ru");
    }

}