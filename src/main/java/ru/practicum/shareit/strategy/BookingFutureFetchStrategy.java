package ru.practicum.shareit.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingFutureFetchStrategy implements BookingStateFetchStrategy {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> fetchByBooker(Long id, Sort sort) {
        LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findAllByBookerIdAndStartAfter(id, time, sort);
    }

    @Override
    public List<Booking> fetchByOwner(Long id, Sort sort) {
        LocalDateTime time = LocalDateTime.now();
        return bookingRepository.findAllByItemOwnerIdAndStartAfter(id, time, sort);
    }
}
