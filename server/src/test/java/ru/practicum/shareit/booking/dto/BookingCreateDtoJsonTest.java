package ru.practicum.shareit.booking.dto;

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
class BookingCreateDtoJsonTest {

    private final JacksonTester<BookingCreateDto> json;

    @Test
    @DisplayName("Сериализация BookingCreteDto")
    void shouldSerialize() throws Exception {
        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(1L);
        bookingCreateDto.setStart(LocalDateTime.of(2024, 11, 5, 10, 0));
        bookingCreateDto.setEnd(LocalDateTime.of(2024, 11, 5, 12, 0));

        JsonContent<BookingCreateDto> result = json.write(bookingCreateDto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo("2024-11-05T10:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo("2024-11-05T12:00:00");

    }

    @Test
    @DisplayName("Десериализация BookingCreateDto")
    void shouldDeserialize() throws Exception {
        String content = "{ \"itemId\": 1, \"start\": \"2024-11-05T10:00:00\", \"end\": \"2024-11-05T12:00:00\" }";

        BookingCreateDto bookingCreateDto = json.parseObject(content);

        assertThat(bookingCreateDto.getItemId())
                .isEqualTo(1);
        assertThat(bookingCreateDto.getStart())
                .isEqualTo(LocalDateTime.of(2024, 11, 5, 10, 0));
        assertThat(bookingCreateDto.getEnd())
                .isEqualTo(LocalDateTime.of(2024, 11, 5, 12, 0));
    }

}