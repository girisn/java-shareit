package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.sql.Timestamp;
import java.time.Instant;

@Component
public class BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Autowired
    public BookingMapper(ItemMapper itemMapper,
                         UserMapper userMapper) {
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
    }

    public Booking convert(BookingDto dto) {
        return new Booking(
                dto.getId(),
                dto.getStart() == null ? null : Timestamp.valueOf(dto.getStart()),
                dto.getEnd() == null ? null : Timestamp.valueOf(dto.getEnd()),
                Booking.Status.valueOf(dto.getStatus()),
                null,
                null,
                Timestamp.from(Instant.now()));
    }

    public BookingDto convert(Booking dto) {
        return new BookingDto(
                dto.getId(),
                null,
                dto.getStart().toLocalDateTime(),
                dto.getEnd().toLocalDateTime(),
                dto.getStatus().name(),
                itemMapper.convert(dto.getItem()),
                userMapper.convert(dto.getBooker()));
    }
}
