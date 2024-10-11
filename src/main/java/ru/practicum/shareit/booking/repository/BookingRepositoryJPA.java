package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepositoryJPA extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT COUNT(*) > 0 " +
            "FROM bookings AS b " +
            "where b.item_id = ?3 " +
            "AND (b.start_date <= ?2 AND b.end_date >= ?1) " +
            "AND b.status != 'REJECTED'", nativeQuery = true)
    Boolean existsBookingByDateAndItemId(LocalDateTime startDate, LocalDateTime endDate, Long itemId);

    @Query(value = "SELECT COUNT(*) > 0 " +
            "FROM bookings AS b " +
            "WHERE b.item_id = ?1 " +
            "AND b.booker_id = ?2 " +
            "AND b.status = 'APPROVED' " +
            "AND b.end_date < CURRENT_TIMESTAMP", nativeQuery = true)
    Boolean existsBookingByItem_IdAndBooker_Id(Long itemId, Long bookerId);

    List<Booking> findBookingsByBookerId(Long bookerId);

    @Query(value = "SELECT * " +
            "FROM bookings AS b " +
            "where  ?2 BETWEEN b.start_date AND b.end_date " +
            "AND (b.status = 'APPROVED' OR b.status = 'WAITING') " +
            "AND b.booker_id = ?1", nativeQuery = true)
    List<Booking> findBookingsCurrent(Long userId, LocalDateTime now);

    @Query(value = "SELECT * " +
            "FROM bookings AS b " +
            "where b.end_date < ?2 " +
            "AND (b.status = 'APPROVED') " +
            "AND b.booker_id = ?1", nativeQuery = true)
    List<Booking> findBookingsPast(Long userId, LocalDateTime now);

    @Query(value = "SELECT * " +
            "FROM bookings AS b " +
            "where  b.start_date > ?2 " +
            "AND (b.status = 'APPROVED' OR b.status = 'WAITING') " +
            "AND b.booker_id = ?1", nativeQuery = true)
    List<Booking> findBookingsFuture(Long userId, LocalDateTime now);

    List<Booking> findBookingsByBooker_IdAndStatus(Long bookerId, BookingStatus status);

    @Query("select bk " +
            "from Booking as bk " +
            "join fetch bk.item " +
            "where bk.item.owner.id = ?1 ")
    List<Booking> findBookingsByOwnerId(Long ownerId);

    @Query(value = "SELECT bk.end_date " +
            "FROM bookings as bk " +
            "JOIN items AS it ON bk.item_id = it.id " +
            "where it.id = ?1 and bk.status = 'APPROVED' and bk.end_date < current_timestamp - interval '2 second' " +
            "order by bk.end_date DESC " +
            "limit 1 ", nativeQuery = true)
    LocalDateTime findLastBookingDateByItemId(Long itemId);

    @Query(value = "SELECT bk.start_date " +
            "FROM bookings as bk " +
            "JOIN items AS it ON bk.item_id = it.id " +
            "where it.id = ?1 and bk.status = 'APPROVED' and bk.start_date > current_timestamp " +
            "order by bk.start_date " +
            "limit 1 ", nativeQuery = true)
    LocalDateTime findNextBookingDateByItemId(Long itemId);
}
