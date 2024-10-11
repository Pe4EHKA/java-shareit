package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingSearchState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepositoryJPA;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJPA;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJPA;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepositoryJPA bookingRepositoryJPA;
    private final UserRepositoryJPA userRepositoryJPA;
    private final ItemRepositoryJPA itemRepositoryJPA;

    @Override
    @Transactional
    public BookingDto addBooking(BookingCreateDto bookingCreateDto, Long bookerId) {
        User booker = userRepositoryJPA.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("User with id " + bookerId + " not found"));
        Item item = itemRepositoryJPA.findById(bookingCreateDto.getItemId())
                .orElseThrow(() ->
                        new NotFoundException("Item  with id" + bookingCreateDto.getItemId() + " not found"));
        if (bookingRepositoryJPA
                .existsBookingByDateAndItemId(bookingCreateDto.getStart(), bookingCreateDto.getEnd(), item.getId())) {
            throw new IllegalArgumentException("Booking on this item with id: + " + bookingCreateDto.getItemId() +
                    " in time between start: " + bookingCreateDto.getStart() + " and end: " +
                    bookingCreateDto.getEnd() + " already exists");
        }

        if (!item.getAvailable()) {
            throw new NotAvailableException("Item is not available");
        }

        item.setAvailable(false);
        itemRepositoryJPA.save(item);

        Booking booking = BookingMapper.toBooking(bookingCreateDto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        booking = bookingRepositoryJPA.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approveBooking(Long bookingId, Long ownerId, Boolean approved) {
        User owner = userRepositoryJPA.findById(ownerId)
                .orElseThrow(() -> new NotAvailableException("User with id " + ownerId + " not found"));
        Booking booking = bookingRepositoryJPA.findById(bookingId)
                .orElseThrow(() -> new NotAvailableException("Booking with id " + bookingId + " not found"));
        if (!booking.getItem().getOwner().equals(owner)) {
            throw new IllegalArgumentException("User with id " + ownerId +
                    " is not owner of item with id: " + booking.getItem().getId());
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        bookingRepositoryJPA.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBooking(Long bookingId, Long userId) {
        User user = userRepositoryJPA.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Booking booking = bookingRepositoryJPA.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id " + bookingId + " not found"));
        if (!(booking.getItem().getOwner().equals(user) || booking.getBooker().getId().equals(userId))) {
            throw new IllegalArgumentException("User with id " + userId +
                    " is not owner/booker of item with id: " + booking.getItem().getId());
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, BookingSearchState state) {
        userRepositoryJPA.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        List<Booking> bookings;
        switch (state) {
            case BookingSearchState.ALL -> bookings = bookingRepositoryJPA.findBookingsByBookerId(userId);
            case BookingSearchState.CURRENT -> bookings = bookingRepositoryJPA.
                    findBookingsCurrent(userId, LocalDateTime.now());
            case BookingSearchState.PAST -> bookings = bookingRepositoryJPA
                    .findBookingsPast(userId, LocalDateTime.now());
            case BookingSearchState.FUTURE -> bookings = bookingRepositoryJPA
                    .findBookingsFuture(userId, LocalDateTime.now());
            case BookingSearchState.WAITING -> bookings = bookingRepositoryJPA
                    .findBookingsByBooker_IdAndStatus(userId, BookingStatus.WAITING);
            case BookingSearchState.REJECTED -> bookings = bookingRepositoryJPA
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
        userRepositoryJPA.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " not found"));
        if (!itemRepositoryJPA.existsItemByOwner_Id(ownerId)) {
            throw new NotFoundException("Owner with id " + ownerId + " doesn't have any items");
        }
        List<Booking> bookings;
        switch (state) {
            case BookingSearchState.ALL -> bookings = bookingRepositoryJPA.findBookingsByOwnerId(ownerId);
            case BookingSearchState.CURRENT -> bookings = bookingRepositoryJPA.
                    findBookingsCurrent(ownerId, LocalDateTime.now());
            case BookingSearchState.PAST -> bookings = bookingRepositoryJPA
                    .findBookingsPast(ownerId, LocalDateTime.now());
            case BookingSearchState.FUTURE -> bookings = bookingRepositoryJPA
                    .findBookingsFuture(ownerId, LocalDateTime.now());
            case BookingSearchState.WAITING -> bookings = bookingRepositoryJPA
                    .findBookingsByBooker_IdAndStatus(ownerId, BookingStatus.WAITING);
            case BookingSearchState.REJECTED -> bookings = bookingRepositoryJPA
                    .findBookingsByBooker_IdAndStatus(ownerId, BookingStatus.REJECTED);
            default -> throw new IllegalArgumentException("Booking search state " + state + " not supported");
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }
}
