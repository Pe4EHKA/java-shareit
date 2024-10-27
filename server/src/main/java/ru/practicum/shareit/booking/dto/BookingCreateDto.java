package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Getter
@ToString
public class BookingCreateDto {
    private Long itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}
