package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingSearchState;
import ru.practicum.shareit.item.Headers;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@Valid @RequestBody BookingCreateDto requestDto,
                                             @RequestHeader(Headers.SHARER_USER_ID) long userId) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.addBooking(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> answerBooking(@PathVariable(name = "bookingId") long bookingId,
                                                @RequestParam(name = "approved") Boolean approved,
                                                @RequestHeader(Headers.SHARER_USER_ID) long ownerId) {
        ResponseEntity<Object> bookingAnswerDto = bookingClient.approveBooking(bookingId, ownerId, approved);
        log.info("Booking approved/rejected: {}", bookingAnswerDto);
        return bookingAnswerDto;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable(name = "bookingId") long bookingId,
                                             @RequestHeader(Headers.SHARER_USER_ID) long userId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
            @RequestHeader(Headers.SHARER_USER_ID) Long userId) {
        BookingSearchState state = BookingSearchState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        ResponseEntity<Object> bookingDtos = bookingClient.getUserBookings(userId, state);
        log.info("User Bookings: {}", bookingDtos);
        return bookingDtos;
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestParam(name = "state",
            defaultValue = "ALL") BookingSearchState state,
                                                   @RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        ResponseEntity<Object> bookingDtos = bookingClient.getOwnerBookings(ownerId, state);
        log.info("Owner Bookings: {}", bookingDtos);
        return bookingDtos;
    }

//    @GetMapping
//    public ResponseEntity<Object> getBookings(@RequestHeader(Headers.SHARER_USER_ID) long userId,
//                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
//                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
//                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
//        BookingSearchState state = BookingSearchState.from(stateParam)
//                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
//        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
//        return bookingClient.getBookings(userId, state, from, size);
//    }

}
