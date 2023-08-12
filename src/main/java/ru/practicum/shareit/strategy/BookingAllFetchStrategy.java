package ru.practicum.shareit.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingAllFetchStrategy implements BookingStateFetchStrategy {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> fetchByBooker(Long id, Sort sort) {
        return bookingRepository.findAllByBookerId(id, sort);
    }

    @Override
    public List<Booking> fetchByOwner(Long id, Sort sort) {
        return bookingRepository.findAllByItemOwnerId(id, sort);
    }
}
