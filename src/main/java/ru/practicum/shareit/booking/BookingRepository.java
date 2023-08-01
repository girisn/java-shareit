package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    List<Booking> findByUserIdAndState(Long userId, String state);

    @Query(value = "select * from Booking b left join Item it on it.id = b.item_id where it.user_id = ?")
    List<Booking> findByOwnerId(Long userId);

    @Query(value = "select * from Booking b left join Item it on it.id = b.item_id where it.user_id = ? and b.state = ?")
    List<Booking> findByOwnerIdAndState(Long userId, String state);
}
