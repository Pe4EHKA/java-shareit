package ru.practicum.shareit.booking.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingSearchState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class BookingServiceImplTest {
    private static final long USER_FIRST_ID = 1L;
    private static final long USER_SECOND_ID = 2L;
    private static final long USER_THIRD_ID = 3L;

    private static final long REQUEST_FIRST_ID = 1L;

    private static final long ITEM_FIRST_ID = 1L;
    private static final long ITEM_SECOND_ID = 2L;

    private static final long BOOKING_FIRST_ID = 1L;
    private static final long BOOKING_SECOND_ID = 2L;
    private static final long BOOKING_THIRD_ID = 3L;
    private static final long BOOKING_FOURTH_ID = 4L;
    private static final long BOOKING_FIFTH_ID = 5L;

    private final EntityManager em;
    private final BookingService bookingService;

    private User userFirst;
    private User userSecond;
    private User userThird;

    private ItemRequest request;

    private Item itemFirst;
    private Item itemSecond;

    private Booking bookingFirst;
    private Booking bookingSecond;
    private Booking bookingThird;
    private Booking bookingFourth;
    private Booking bookingFifth;

    @BeforeEach
    void setUp() {
        userFirst = new User();
        userFirst.setId(USER_FIRST_ID);
        userFirst.setName("userFirst");
        userFirst.setEmail("userFirst@mail.ru");

        userSecond = new User();
        userSecond.setId(USER_SECOND_ID);
        userSecond.setName("userSecond");
        userSecond.setEmail("userSecond@mail.ru");

        userThird = new User();
        userThird.setId(USER_THIRD_ID);
        userThird.setName("userThird");
        userThird.setEmail("userThird@mail.ru");

        request = new ItemRequest();
        request.setId(REQUEST_FIRST_ID);
        request.setDescription("needSecondItem");
        request.setCreated(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
        request.setRequester(userSecond);

        itemFirst = new Item();
        itemFirst.setId(ITEM_FIRST_ID);
        itemFirst.setName("itemFirst");
        itemFirst.setDescription("itemFirstDescription");
        itemFirst.setAvailable(true);
        itemFirst.setOwner(userFirst);

        itemSecond = new Item();
        itemSecond.setId(ITEM_SECOND_ID);
        itemSecond.setName("itemSecond");
        itemSecond.setDescription("itemSecondDescription");
        itemSecond.setAvailable(false);
        itemSecond.setOwner(userFirst);
        itemSecond.setRequest(request);

        bookingFirst = new Booking();
        bookingFirst.setId(BOOKING_FIRST_ID);
        bookingFirst.setStart(LocalDateTime.of(2024, 1, 1, 11, 0, 0));
        bookingFirst.setEnd(LocalDateTime.of(2024, 1, 1, 11, 1, 0));
        bookingFirst.setItem(itemSecond);
        bookingFirst.setBooker(userSecond);
        bookingFirst.setStatus(BookingStatus.WAITING);

        bookingSecond = new Booking();
        bookingSecond.setId(BOOKING_SECOND_ID);
        bookingSecond.setStart(LocalDateTime.of(2024, 1, 1, 11, 0, 0));
        bookingSecond.setEnd(LocalDateTime.of(2024, 1, 1, 11, 10, 0));
        bookingSecond.setItem(itemFirst);
        bookingSecond.setBooker(userThird);
        bookingSecond.setStatus(BookingStatus.APPROVED);

        bookingThird = new Booking();
        bookingThird.setId(BOOKING_THIRD_ID);
        bookingThird.setStart(LocalDateTime.of(2000, 1, 1, 11, 0, 0));
        bookingThird.setEnd(LocalDateTime.of(2000, 1, 1, 12, 0, 0));
        bookingThird.setItem(itemFirst);
        bookingThird.setBooker(userThird);
        bookingThird.setStatus(BookingStatus.APPROVED);

        bookingFourth = new Booking();
        bookingFourth.setId(BOOKING_FOURTH_ID);
        bookingFourth.setStart(LocalDateTime.of(2075, 1, 1, 11, 0, 0));
        bookingFourth.setEnd(LocalDateTime.of(2075, 1, 1, 12, 0, 0));
        bookingFourth.setItem(itemFirst);
        bookingFourth.setBooker(userThird);
        bookingFourth.setStatus(BookingStatus.APPROVED);

        bookingFifth = new Booking();
        bookingFifth.setId(BOOKING_FIFTH_ID);
        bookingFifth.setStart(LocalDateTime.of(2025, 1, 1, 11, 0, 0));
        bookingFifth.setEnd(LocalDateTime.of(2025, 1, 1, 11, 0, 50));
        bookingFifth.setItem(itemSecond);
        bookingFifth.setBooker(userThird);
        bookingFifth.setStatus(BookingStatus.REJECTED);
    }

    @Test
    @DisplayName("Сервис бронирования: добавление бронирования")
    void shouldAddBooking() {
        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(ITEM_FIRST_ID);
        bookingCreateDto.setStart(LocalDateTime.of(2024, 1, 1, 13, 0, 0));
        bookingCreateDto.setEnd(LocalDateTime.of(2024, 1, 1, 13, 50, 0));

        bookingService.addBooking(bookingCreateDto, USER_THIRD_ID);

        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        query.setParameter("id", BOOKING_FIFTH_ID + 1);

        Booking result = query.getSingleResult();

        assertEquals(bookingCreateDto.getItemId(), result.getItem().getId());
        assertEquals(bookingCreateDto.getStart(), result.getStart());
        assertEquals(bookingCreateDto.getEnd(), result.getEnd());
    }

    @Test
    @DisplayName("Сервис бронирования: Одобрение бронирования")
    void approveBooking() {
        bookingService.approveBooking(BOOKING_FIRST_ID, USER_FIRST_ID, true);

        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking b WHERE b.id = :id", Booking.class);
        query.setParameter("id", BOOKING_FIRST_ID);

        Booking result = query.getSingleResult();

        assertEquals(result.getStatus(), BookingStatus.APPROVED);
    }

    @Test
    @DisplayName("Сервис бронирования: Получение бронирования")
    void shouldGetBooking() {
        BookingDto bookingDto = bookingService.getBooking(BOOKING_FIRST_ID, USER_FIRST_ID);

        assertEquals(BookingMapper.toBookingDto(bookingFirst), bookingDto);
    }

    @Test
    @DisplayName("Сервис бронирования: получение всех бронирований пользователя")
    void shouldGetUserBookingsAll() {
        List<BookingDto> bookingDtos = bookingService.getUserBookings(USER_THIRD_ID, BookingSearchState.ALL);

        assertEquals(Stream.of(bookingSecond, bookingThird, bookingFourth, bookingFifth)
                .map(BookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart))
                .toList(), bookingDtos);
    }

    @Test
    @DisplayName("Сервис бронирования: получение текущих бронирований пользователя")
    void shouldGetUserBookingsCurrent() {
        List<BookingDto> bookingDtos = bookingService.getUserBookings(USER_THIRD_ID, BookingSearchState.CURRENT);

        assertEquals(List.of(), bookingDtos);
    }

    @Test
    @DisplayName("Сервис бронирования: получение прошедших бронирований пользователя")
    void shouldGetUserBookingsPast() {
        List<BookingDto> bookingDtos = bookingService.getUserBookings(USER_THIRD_ID, BookingSearchState.PAST);

        assertEquals(Stream.of(bookingSecond, bookingThird)
                .map(BookingMapper::toBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart))
                .toList(), bookingDtos);
    }

    @Test
    @DisplayName("Сервис бронирования: получение будующих бронирований пользователя")
    void shouldGetUserBookingsFuture() {
        List<BookingDto> bookingDtos = bookingService.getUserBookings(USER_THIRD_ID, BookingSearchState.FUTURE);

        assertEquals(List.of(BookingMapper.toBookingDto(bookingFourth)), bookingDtos);
    }

    @Test
    @DisplayName("Сервис бронирования: получение ожидающих одобрения/отклонения бронирований пользователя")
    void shouldGetUserBookingsWaiting() {
        List<BookingDto> bookingDtos = bookingService.getUserBookings(USER_SECOND_ID, BookingSearchState.WAITING);

        assertEquals(List.of(BookingMapper.toBookingDto(bookingFirst)), bookingDtos);
    }

    @Test
    @DisplayName("Сервис бронирования: получение отклоненных бронирований пользователя")
    void shouldGetUserBookingsRejected() {
        List<BookingDto> bookingDtos = bookingService.getUserBookings(USER_THIRD_ID, BookingSearchState.REJECTED);

        assertEquals(List.of(BookingMapper.toBookingDto(bookingFifth)), bookingDtos);
    }

    @Test
    @DisplayName("Сервис бронирования: получение всех бронирований собственника")
    void shouldGetOwnerBookingsAll() {
        List<BookingDto> bookingDtos = bookingService.getOwnerBookings(USER_FIRST_ID, BookingSearchState.ALL);

        assertEquals(Stream.of(bookingFirst, bookingSecond, bookingThird, bookingFourth, bookingFifth)
                        .map(BookingMapper::toBookingDto)
                        .toList(),
                bookingDtos.stream()
                        .sorted(Comparator.comparing(BookingDto::getId))
                        .toList());
    }

    @Test
    @DisplayName("Сервис бронирования: получение текущих бронирований собственника")
    void shouldGetOwnerBookingsCurrent() {
        List<BookingDto> bookingDtos = bookingService.getOwnerBookings(USER_FIRST_ID, BookingSearchState.CURRENT);

        assertEquals(List.of(), bookingDtos);
    }

    @Test
    @DisplayName("Сервис бронирования: получение прошедших бронирований собственника")
    void shouldGetOwnerBookingsPast() {
        List<BookingDto> bookingDtos = bookingService.getOwnerBookings(USER_FIRST_ID, BookingSearchState.PAST);

        assertEquals(Stream.of(bookingSecond, bookingThird)
                        .map(BookingMapper::toBookingDto)
                        .toList(),
                bookingDtos.stream()
                        .sorted(Comparator.comparing(BookingDto::getId))
                        .toList());
    }

    @Test
    @DisplayName("Сервис бронирования: получение будущих бронирований собственника")
    void shouldGetOwnerBookingsFuture() {
        List<BookingDto> bookingDtos = bookingService.getOwnerBookings(USER_FIRST_ID, BookingSearchState.FUTURE);

        assertEquals(List.of(BookingMapper.toBookingDto(bookingFourth)), bookingDtos);
    }

    @Test
    @DisplayName("Сервис бронирования: получение ожидающих одобрения/отклонения бронирований собственника")
    void shouldGetOwnerBookingsWaiting() {
        List<BookingDto> bookingDtos = bookingService.getOwnerBookings(USER_FIRST_ID, BookingSearchState.WAITING);

        assertEquals(List.of(BookingMapper.toBookingDto(bookingFirst)), bookingDtos);
    }

    @Test
    @DisplayName("Сервис бронирования: получение отклоненных бронирований собственника")
    void shouldGetOwnerBookingsRejected() {
        List<BookingDto> bookingDtos = bookingService.getOwnerBookings(USER_FIRST_ID, BookingSearchState.REJECTED);

        assertEquals(List.of(BookingMapper.toBookingDto(bookingFifth)), bookingDtos);
    }

    @Test
    @DisplayName("Exception: User not found")
    void shouldThrowNotFoundExceptionWhenUserNotFound() {
        assertThrows(NotFoundException.class, () -> {
            bookingService.getUserBookings(999L, BookingSearchState.ALL);
        });
    }

    @Test
    @DisplayName("Exception: Item not found")
    void shouldThrowNotFoundExceptionWhenItemNotFound() {
        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(999L);
        bookingCreateDto.setStart(LocalDateTime.now().plusHours(1));
        bookingCreateDto.setEnd(LocalDateTime.now().plusHours(2));

        assertThrows(NotFoundException.class, () -> {
            bookingService.addBooking(bookingCreateDto, USER_FIRST_ID);
        });
    }

    @Test
    @DisplayName("Exception: Booking conflict - Already booked during time range")
    void shouldThrowIllegalArgumentExceptionWhenBookingConflictExists() {
        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(ITEM_FIRST_ID);
        bookingCreateDto.setStart(LocalDateTime.of(2024, 1, 1, 11, 5, 0));
        bookingCreateDto.setEnd(LocalDateTime.of(2024, 1, 1, 11, 15, 0));

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.addBooking(bookingCreateDto, USER_THIRD_ID);
        });
    }

    @Test
    @DisplayName("Exception: Item not available")
    void shouldThrowNotAvailableExceptionWhenItemUnavailable() {
        BookingCreateDto bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(ITEM_SECOND_ID);
        bookingCreateDto.setStart(LocalDateTime.of(2024, 1, 2, 10, 0, 0));
        bookingCreateDto.setEnd(LocalDateTime.of(2024, 1, 2, 12, 0, 0));

        assertThrows(NotAvailableException.class, () -> {
            bookingService.addBooking(bookingCreateDto, USER_FIRST_ID);
        });
    }

    @Test
    @DisplayName("Exception: User not owner or booker")
    void shouldThrowIllegalArgumentExceptionWhenUserNotOwnerOrBooker() {
        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.getBooking(BOOKING_FIRST_ID, USER_THIRD_ID);
        });
    }

    @Test
    @DisplayName("Exception: Owner has no items")
    void shouldThrowNotFoundExceptionWhenOwnerHasNoItems() {
        assertThrows(NotFoundException.class, () -> {
            bookingService.getOwnerBookings(999L, BookingSearchState.ALL);
        });
    }
}