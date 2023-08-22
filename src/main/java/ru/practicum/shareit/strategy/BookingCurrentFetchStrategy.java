package ru.practicum.shareit.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingCurrentFetchStrategy implements BookingStateFetchStrategy {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> fetchByBooker(Long id, Pageable pageable) {
        LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(id, time, time, pageable);
    }

    @Override
    public List<Booking> fetchByOwner(Long id, Pageable pageable) {
        LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfter(id, time, time, pageable);
    }
}
