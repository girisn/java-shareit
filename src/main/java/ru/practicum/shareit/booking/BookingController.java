package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody InputBookingDto bookingDto,
                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        return new ResponseEntity<>(bookingService.save(bookingDto, userId), HttpStatus.CREATED);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<?> approve(@PathVariable long bookingId,
                                     @RequestParam Boolean approved,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        return new ResponseEntity<>(bookingService.approve(userId, bookingId, approved), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<?> getById(@PathVariable long bookingId,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        return new ResponseEntity<>(bookingService.getById(bookingId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllForBooker(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestParam(required = false, defaultValue = "ALL") String state) {
        return new ResponseEntity<>(bookingService.findAllForBooker(userId, state), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getAllForOwner(@RequestHeader("X-Sharer-User-Id") long owner,
                                            @RequestParam(required = false, defaultValue = "ALL") String state) {
        return new ResponseEntity<>(bookingService.findAllForOwner(owner, state), HttpStatus.OK);
    }
}
