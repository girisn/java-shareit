package ru.practicum.shareit.strategy;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.Booking;

import java.util.List;

public interface BookingStateFetchStrategy {
    public List<Booking> fetchByBooker(Long id, Sort sort);

    public List<Booking> fetchByOwner(Long id, Sort sort);
}
