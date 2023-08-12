package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;

@Component
public class BookingMapper {
    public Booking bookingToBookingShortDto(BookingShortDto bookingShortDto) {
        return Booking.builder()
                .id(bookingShortDto.getId())
                .start(bookingShortDto.getStart())
                .end(bookingShortDto.getEnd())
                .build();
    }

    public BookingItemDto bookingToItemBookingDto(Booking booking) {
        return BookingItemDto.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }

    public BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(new BookingDto.UserDto(booking.getBooker().getId(), booking.getBooker().getName()))
                .status(booking.getStatus())
                .item(new BookingDto.ItemDto(booking.getItem().getId(), booking.getItem().getName()))
                .build();
    }
}
