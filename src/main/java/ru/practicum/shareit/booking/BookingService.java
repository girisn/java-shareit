package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.Booking.Status.APPROVED;
import static ru.practicum.shareit.booking.Booking.Status.REJECTED;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingService(BookingMapper mapper,
                          UserRepository userRepository,
                          ItemRepository itemRepository,
                          BookingRepository repository) {
        this.bookingMapper = mapper;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingRepository = repository;
    }

    public BookingDto save(BookingDto bookingDto, long userId) {
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User booker = getUser(userId);
        if (item.getOwner().getId() == userId) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (bookingDto.getEnd() == null || bookingDto.getStart() == null
                || bookingDto.getStart().equals(bookingDto.getEnd())
                || bookingDto.getStart().isAfter(bookingDto.getEnd())
                || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Booking booking = bookingMapper.convert(bookingDto);
        booking.setItem(item);
        booking.setBooker(booker);
        if (!booking.getItem().getAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return bookingMapper.convert(bookingRepository.save(booking));
    }

    public BookingDto approve(long userId, long bookingId, boolean status) {
        getUser(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (booking.getStatus().equals(APPROVED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        booking.setStatus(status ? APPROVED : REJECTED);
        return bookingMapper.convert(bookingRepository.save(booking));
    }

    public BookingDto getById(long bookingId, long userId) {
        getUser(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        long id = booking.getItem().getId();
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (booking.getBooker().getId() != userId) {
            if (item.getOwner().getId() != userId) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }

        return bookingMapper.convert(booking);
    }

    public List<BookingDto> findAllForBooker(long bookerId, String state) {
        getUser(bookerId);

        return findBookingsForBooking(state, bookerId)
                .stream()
                .map(bookingMapper::convert)
                .collect(Collectors.toList());
    }

    public List<BookingDto> findAllForOwner(long ownerId, String state) {
        getUser(ownerId);
        if (itemRepository.findAllByOwnerId(ownerId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return findBookingsForOwner(state, ownerId)
                .stream()
                .map(bookingMapper::convert)
                .collect(Collectors.toList());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<Booking> findBookingsForOwner(String state, long id) {
        List<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(id);
                return bookings;
            case "WAITING":
            case "APPROVED":
            case "REJECTED":
            case "CANCELED":
                Booking.Status status = null;

                for (Booking.Status value : Booking.Status.values()) {
                    if (value.name().equals(state)) {
                        status = value;
                    }
                }
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(id, status);
                break;
            case "PAST":
                bookings = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(id, Timestamp.from(Instant.now()));
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(id, Timestamp.from(Instant.now()));
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentOwnerBookings(id, Timestamp.from(Instant.now()));
                break;

            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings;
    }

    public List<Booking> findBookingsForBooking(String state, long id) {
        List<Booking> bookings;

        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(id);
                return bookings;
            case "WAITING":
            case "APPROVED":
            case "REJECTED":
            case "CANCELED":
                Booking.Status status = null;
                for (Booking.Status value : Booking.Status.values()) {
                    if (value.name().equals(state)) {
                        status = value;
                    }
                }
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(id, status);
                break;
            case "PAST":
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(id, Timestamp.from(Instant.now()));
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(id, Timestamp.from(Instant.now()));
                break;
            case "CURRENT":
                bookings = bookingRepository.findCurrentBookerBookings(id, Timestamp.from(Instant.now()));
                break;
            default:
                throw new BadRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings;
    }
}
