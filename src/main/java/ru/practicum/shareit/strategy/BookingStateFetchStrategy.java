package ru.practicum.shareit.strategy;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;

import java.util.List;

public interface BookingStateFetchStrategy {
    public List<Booking> fetchByBooker(Long id, Pageable pageable);

    public List<Booking> fetchByOwner(Long id, Pageable pageable);
}
