package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingSearchState;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingCreateDto bookingCreateDto, Long bookerId);

    BookingDto approveBooking(Long bookingId, Long ownerId, Boolean approved);

    BookingDto getBooking(Long bookingId, Long userId);

    List<BookingDto> getUserBookings(Long userId, BookingSearchState state);

    List<BookingDto> getOwnerBookings(Long ownerId, BookingSearchState state);
}
