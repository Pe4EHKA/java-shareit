package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingSearchState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDto addBooking(BookingCreateDto bookingCreateDto, Long bookerId) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("User with id " + bookerId + " not found"));
        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() ->
                        new NotFoundException("Item  with id" + bookingCreateDto.getItemId() + " not found"));
        if (bookingRepository
                .existsBookingByDateAndItemId(bookingCreateDto.getStart(), bookingCreateDto.getEnd(), item.getId())) {
            throw new IllegalArgumentException("Booking on this item with id: + " + bookingCreateDto.getItemId() +
                    " in time between start: " + bookingCreateDto.getStart() + " and end: " +
                    bookingCreateDto.getEnd() + " already exists");
        }

        if (!item.getAvailable()) {
            throw new NotAvailableException("Item is not available");
        }

        item.setAvailable(false);
        itemRepository.save(item);

        Booking booking = BookingMapper.toBooking(bookingCreateDto, item, booker);


        booking = bookingRepository.save(booking);
        log.info("Saved booking: {}", booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approveBooking(Long bookingId, Long ownerId, Boolean approved) {
        Booking booking = bookingRepository.findByIdAndItem_OwnerId(bookingId, ownerId)
                .orElseThrow(() -> new NotAvailableException("Booking with id " + bookingId +
                        " and item ownerId: " + ownerId + " not found "));
        if (!approved) {
            Item item = booking.getItem();
            item.setAvailable(true);
            itemRepository.save(item);
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBooking(Long bookingId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));
        if (!(booking.getItem().getOwner().equals(user) || booking.getBooker().getId().equals(userId))) {
            throw new IllegalArgumentException("User with id " + userId +
                    " is not owner/booker of item with id: " + booking.getItem().getId());
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, BookingSearchState state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        List<Booking> bookings;
        Instant now = Instant.now();
        switch (state) {
            case BookingSearchState.ALL -> bookings = bookingRepository.findBookingsByBookerId(userId);
            case BookingSearchState.CURRENT -> bookings = bookingRepository
                    .findBookingsCurrent(userId, now);
            case BookingSearchState.PAST -> bookings = bookingRepository
                    .findBookingsPast(userId, now);
            case BookingSearchState.FUTURE -> bookings = bookingRepository
                    .findBookingsFuture(userId, now);
            case BookingSearchState.WAITING -> bookings = bookingRepository
                    .findBookingsByBooker_IdAndStatus(userId, BookingStatus.WAITING);
            case BookingSearchState.REJECTED -> bookings = bookingRepository
                    .findBookingsByBooker_IdAndStatus(userId, BookingStatus.REJECTED);
            default -> throw new IllegalArgumentException("Booking search state " + state + " not supported");
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart))
                .toList();
    }

    @Override
    public List<BookingDto> getOwnerBookings(Long ownerId, BookingSearchState state) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " not found"));
        if (!itemRepository.existsItemByOwner_Id(ownerId)) {
            throw new NotFoundException("Owner with id " + ownerId + " doesn't have any items");
        }
        List<Booking> bookings;
        switch (state) {
            case BookingSearchState.ALL -> bookings = bookingRepository.findBookingsByOwnerId(ownerId);
            case BookingSearchState.CURRENT -> bookings = bookingRepository
                    .findBookingsCurrent(ownerId, Instant.now());
            case BookingSearchState.PAST -> bookings = bookingRepository
                    .findBookingsPast(ownerId, Instant.now());
            case BookingSearchState.FUTURE -> bookings = bookingRepository
                    .findBookingsFuture(ownerId, Instant.now());
            case BookingSearchState.WAITING -> bookings = bookingRepository
                    .findBookingsByBooker_IdAndStatus(ownerId, BookingStatus.WAITING);
            case BookingSearchState.REJECTED -> bookings = bookingRepository
                    .findBookingsByBooker_IdAndStatus(ownerId, BookingStatus.REJECTED);
            default -> throw new IllegalArgumentException("Booking search state " + state + " not supported");
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }
}
