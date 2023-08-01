package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.common.CrudService;
import ru.practicum.shareit.common.DtoMapper;

import java.util.List;
import java.util.stream.Collectors;

public class BookingService extends CrudService<Long, Booking, BookingDto> {
    private final BookingRepository bookingRepository;

    public BookingService(DtoMapper<Booking, BookingDto> mapper,
                          BookingRepository repository) {
        super(mapper, repository);
        this.bookingRepository = repository;
    }

    @Override
    protected Void validate(Booking model) {
        return null;
    }

    public BookingDto findById(Long userId, Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!booking.getItem().getUser().getId().equals(userId)
                && !booking.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
        return mapper.convert(booking);
    }

    public List<BookingDto> findByState(Long userId, BookingState state) {
        List<Booking> bookings;
        if (state == BookingState.ALL) {
            bookings = bookingRepository.findByUserId(userId);
        } else {
            bookings = bookingRepository.findByUserIdAndState(userId, state.name());
        }
        return bookings.stream().map(mapper::convert).collect(Collectors.toList());
    }

    public List<BookingDto> findByStateOwner(Long userId, BookingState state) {
        List<Booking> bookings;
        if (state == BookingState.ALL) {
            bookings = bookingRepository.findByOwnerId(userId);
        } else {
            bookings = bookingRepository.findByOwnerIdAndState(userId, state.name());
        }
        return bookings.stream().map(mapper::convert).collect(Collectors.toList());
    }

    public BookingDto approve(Long userId, Long id, boolean approved) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!booking.getItem().getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }

        if (approved) {
            booking.setStatus(Booking.Status.APPROVED);
        } else {
            booking.setStatus(Booking.Status.REJECTED);
        }
        return mapper.convert(bookingRepository.save(booking));
    }
}
