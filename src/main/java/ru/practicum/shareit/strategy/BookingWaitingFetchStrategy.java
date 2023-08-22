package ru.practicum.shareit.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingWaitingFetchStrategy implements BookingStateFetchStrategy {
    private final BookingRepository bookingRepository;

    @Override
    public List<Booking> fetchByBooker(Long id, Pageable pageable) {
        return bookingRepository.findAllByBookerIdAndStatus(id, Status.WAITING, pageable);
    }

    @Override
    public List<Booking> fetchByOwner(Long id, Pageable pageable) {
        return bookingRepository.findAllByItemOwnerIdAndStatus(id, Status.WAITING, pageable);
    }
}
