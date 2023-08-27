package ru.practicum.shareitgateway.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareitgateway.booking.dto.BookItemRequestDto;
import ru.practicum.shareitgateway.booking.dto.BookingState;
import ru.practicum.shareitgateway.client.BaseClient;

import java.util.Map;

@Service
@Slf4j
public class BookingClient extends BaseClient {
    @Autowired
    public BookingClient(@Qualifier("bookings") RestTemplate restTemplate) {
        super(restTemplate);
    }

    public ResponseEntity<Object> createBooking(long userId, BookItemRequestDto requestDto) {
        log.info("Create booking for user with id = {} ", userId);
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        log.info("Get all booking for user with id = {} ", userId);
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        log.info("Get booking for user with id = {} with booking id = {}", userId, bookingId);
        return get("/" + bookingId, userId);
    }


    public ResponseEntity<Object> getBookingByOwner(long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        log.info("Get all booking for owner with id = {} ", userId);
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> approveBooking(long userId, Long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved);
        log.info("Get all booking for user with id = {} with booking id = {}", userId, bookingId);
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }
}
