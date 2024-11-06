package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoJsonTest {

    private final JacksonTester<BookingDto> json;

    @Test
    @DisplayName("Сериализация BookingDto")
    void shouldSerialize() throws Exception {
        ItemDto item = new ItemDto();
        item.setId(1L);
        item.setName("Test Item");

        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("Test User");
        user.setEmail("test.user@example.com");

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(3L);
        bookingDto.setStart(LocalDateTime.of(2024, 11, 4, 12, 0));
        bookingDto.setEnd(LocalDateTime.of(2024, 11, 4, 14, 0));
        bookingDto.setItem(item);
        bookingDto.setBooker(user);
        bookingDto.setStatus(BookingStatus.APPROVED);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(3);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo("2024-11-04T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo("2024-11-04T14:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name")
                .isEqualTo("Test Item");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id")
                .isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.booker.name")
                .isEqualTo("Test User");
        assertThat(result).extractingJsonPathStringValue("$.booker.email")
                .isEqualTo("test.user@example.com");
        assertThat(result).extractingJsonPathStringValue("$.status")
                .isEqualTo("APPROVED");
    }

    @Test
    @DisplayName("Десериализация BookingDto")
    void shouldDeserialize() throws Exception {
        String content = "{ \"id\": 3, \"start\": \"2024-11-04T12:00:00\", \"end\": \"2024-11-04T14:00:00\", " +
                "\"item\": { \"id\": 1, \"name\": \"Test Item\" }, " +
                "\"booker\": { \"id\": 2, \"name\": \"Test User\", \"email\": \"test.user@example.com\" }, " +
                "\"status\": \"APPROVED\" }";

        BookingDto bookingDto = json.parseObject(content);

        assertThat(bookingDto.getId())
                .isEqualTo(3);
        assertThat(bookingDto.getStart())
                .isEqualTo(LocalDateTime.of(2024, 11, 4, 12, 0));
        assertThat(bookingDto.getEnd())
                .isEqualTo(LocalDateTime.of(2024, 11, 4, 14, 0));
        assertThat(bookingDto.getItem().getId())
                .isEqualTo(1);
        assertThat(bookingDto.getItem().getName())
                .isEqualTo("Test Item");
        assertThat(bookingDto.getBooker().getId())
                .isEqualTo(2);
        assertThat(bookingDto.getBooker().getName())
                .isEqualTo("Test User");
        assertThat(bookingDto.getBooker().getEmail())
                .isEqualTo("test.user@example.com");
        assertThat(bookingDto.getStatus())
                .isEqualTo(BookingStatus.APPROVED);
    }
}