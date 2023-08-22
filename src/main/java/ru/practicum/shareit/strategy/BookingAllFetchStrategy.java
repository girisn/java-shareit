package ru.practicum.shareit.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingAllFetchStrategy implements BookingStateFetchStrategy {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> fetchByBooker(Long id, Pageable pageable) {
        return bookingRepository.findAllByBookerId(id, pageable);
    }

    @Override
    public List<Booking> fetchByOwner(Long id, Pageable pageable) {
        return bookingRepository.findAllByItemOwnerId(id, pageable);
    }
}
