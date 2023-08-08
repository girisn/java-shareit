package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(long bookerId);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(long ownerId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long bookerId, Booking.Status status);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long ownerId, Booking.Status status);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long bookerId, Timestamp now);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(long ownerId, Timestamp now);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long bookerId, Timestamp now);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(long ownerId, Timestamp now);

    Long countAllByItemIdAndBookerIdAndEndBefore(long itemId, long userId, Timestamp now);

    List<Booking> findAllByItemIdAndStartBeforeOrderByStartDesc(long itemId, Timestamp now);

    List<Booking> findAllByItemIdAndStartAfterOrderByStartDesc(long itemId, Timestamp now);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 and " +
            "b.item.owner.id = ?2 and " +
            "b.end < ?3 order by b.start desc")
    List<Booking> findPastOwnerBookings(long itemId, long ownerId, Timestamp now);

    @Query("select b from Booking b " +
            "where b.item.id = ?1 and " +
            "b.item.owner.id = ?2 and " +
            "b.start > ?3 " +
            "order by b.start desc")
    List<Booking> findFutureOwnerBookings(long itemId, long ownerId, Timestamp now);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 and " +
            "b.start < ?2 and " +
            "b.end > ?2 " +
            "order by b.start desc")
    List<Booking> findCurrentBookerBookings(long bookerId, Timestamp now);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 and " +
            "b.start < ?2 and " +
            "b.end > ?2 " +
            "order by b.start desc")
    List<Booking> findCurrentOwnerBookings(long ownerId, Timestamp now);
}
