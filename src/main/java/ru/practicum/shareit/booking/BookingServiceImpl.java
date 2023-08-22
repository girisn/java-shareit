package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.strategy.BookingStateFetchStrategy;
import ru.practicum.shareit.strategy.BookingStateStrategy;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.State.validateState;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingStateStrategy bookingStateStrategy;

    @Override
    public BookingDto createBooking(Long userId, BookingShortDto bookingShortDto) {
        Item item = itemRepository.findById(bookingShortDto.getItemId()).orElseThrow(() ->
                new NotFoundException("Item with id= " + userId + " hasn't not found"));
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Not possible create Booking - " +
                        "Not found User with Id " + userId));

        if (bookingShortDto.getStart().isEqual(bookingShortDto.getEnd()) ||
                bookingShortDto.getStart().isAfter(bookingShortDto.getEnd())) {
            throw new NotAvailableException("The end day can't be earlier than the start day");
        }
        if (userId.equals(item.getOwner().getId())) {
            throw new NotFoundException("Your own item with id= " + userId + " can't book");
        }
        if (!item.getAvailable()) {
            throw new NotAvailableException("Item with id= " + userId + " isn't not available");
        }

        Booking booking = BookingMapper.bookingToBookingShortDto(bookingShortDto);
        booking.setBooker(userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User with id= " + userId + " hasn't not found")));
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        return BookingMapper.bookingToBookingDto(bookingRepository.save(booking));
    }

    @Transactional(readOnly = true)
    @Override
    public BookingDto getBookingById(Long id, Long bookingId) {
        Booking booking = validateBooking(bookingId);
        if (!booking.getBooker().getId().equals(id) && !booking.getItem().getOwner().getId().equals(id)) {
            throw new NotFoundException("User with id= " + id + " is not booker or owner");
        }
        return BookingMapper.bookingToBookingDto(booking);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllBookingByState(Long id, String stateString, int from, int size) {
        validateUser(id);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        List<Booking> bookingList;
        LocalDateTime time = LocalDateTime.now();

        State state = validateState(stateString);
        BookingStateFetchStrategy strategy = bookingStateStrategy.handlers.get(state);
        if (strategy == null) {
            bookingList = Collections.emptyList();
        } else {
            bookingList = strategy.fetchByBooker(id, pageable);
        }

        log.info("Get booking with state  = {}", state);
        return bookingList.stream()
                .map(BookingMapper::bookingToBookingDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookingDto> getAllOwnersBookingByState(Long id, String stateString, int from, int size) {
        validateUser(id);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));
        List<Booking> bookingList;
        LocalDateTime now = LocalDateTime.now();

        State state = validateState(stateString);
        BookingStateFetchStrategy strategy = bookingStateStrategy.handlers.get(state);
        if (strategy == null) {
            bookingList = Collections.emptyList();
        } else {
            bookingList = strategy.fetchByOwner(id, pageable);
        }

        log.info("Get all owners booking with state  = {}", state);
        return bookingList.stream()
                .map(BookingMapper::bookingToBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto approveBooking(Long id, Long bookingId, Boolean approved) {

        Booking booking = validateBooking(bookingId);

        if (!booking.getItem().getOwner().getId().equals(id)) {
            throw new NotFoundException("User with id= " + id + " is not owner");
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new NotAvailableException("Item with id= " + bookingId + " has been approved yet");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        log.info("Get approve booking with id  = {}", bookingId);
        return BookingMapper.bookingToBookingDto((booking));
    }

    private Booking validateBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Booking with id= " + bookingId + " hasn't not found"));
    }

    private void validateUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id= " + id + " hasn't not found");
        }
    }
}