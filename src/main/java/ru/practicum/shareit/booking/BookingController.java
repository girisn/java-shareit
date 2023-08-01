package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestBody BookingDto booking) {
        return bookingService.save(booking);
    }

    @PatchMapping("/{id}")
    public BookingDto patch(@RequestHeader("X-Sharer-User-Id") Long userId,
                            @PathVariable Long id,
                            @RequestParam boolean approved) {
        return bookingService.approve(userId, id, approved);
    }

    @GetMapping("/{id}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long id) {
        return bookingService.findById(userId, id);
    }

    @GetMapping
    public List<BookingDto> findAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.findByState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> findAllOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.findByStateOwner(userId, state);
    }
}
