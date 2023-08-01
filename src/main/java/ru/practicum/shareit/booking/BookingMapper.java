package ru.practicum.shareit.booking;

import ru.practicum.shareit.common.DtoMapper;

import java.sql.Timestamp;

public class BookingMapper implements DtoMapper<Booking, BookingDto> {
    @Override
    public Booking convert(BookingDto dto) {
        return new Booking(
                dto.getId(),
                Timestamp.valueOf(dto.getStart()),
                Timestamp.valueOf(dto.getEnd()),
                dto.getStatus(),
                null,
                null);
    }

    @Override
    public BookingDto convert(Booking dto) {
        return new BookingDto(
                dto.getId(),
                dto.getStart().toLocalDateTime(),
                dto.getEnd().toLocalDateTime(),
                dto.getItem().getId(),
                dto.getStatus());
    }

    @Override
    public Booking update(Booking user, BookingDto dto) {
        return this.convert(dto);
    }
}
