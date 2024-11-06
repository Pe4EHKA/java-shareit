package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.Headers;

import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestBody BookingCreateDto bookingCreateDto,
                                 @RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        BookingDto bookingDto = bookingService.addBooking(bookingCreateDto, ownerId);
        log.info("Booking created: {}", bookingDto);
        return bookingDto;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto answerBooking(@PathVariable(name = "bookingId") Long bookingId,
                                    @RequestParam(name = "approved") Boolean approved,
                                    @RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        BookingDto bookingDto = bookingService.approveBooking(bookingId, ownerId, approved);
        log.info("Booking approved/rejected: {}", bookingDto);
        return bookingDto;
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable(name = "bookingId") Long bookingId,
                                 @RequestHeader(Headers.SHARER_USER_ID) Long userId) {
        BookingDto bookingDto = bookingService.getBooking(bookingId, userId);
        log.info("Booking: {}", bookingDto);
        return bookingDto;
    }

    @GetMapping
    public Collection<BookingDto> getUserBookings(
            @RequestParam(name = "state", defaultValue = "ALL") BookingSearchState state,
            @RequestHeader(Headers.SHARER_USER_ID) Long userId) {
        List<BookingDto> bookingDtos = bookingService.getUserBookings(userId, state);
        log.info("User Bookings: {}", bookingDtos);
        return bookingDtos;
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getOwnerBookings(@RequestParam(name = "state",
            defaultValue = "ALL") BookingSearchState state,
                                                   @RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        List<BookingDto> bookingDtos = bookingService.getOwnerBookings(ownerId, state);
        log.info("Owner Bookings: {}", bookingDtos);
        return bookingDtos;
    }
}
