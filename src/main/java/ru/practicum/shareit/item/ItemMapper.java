package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;

import java.util.List;

@Component
public class ItemMapper {
    public Item convert(ItemDto dto) {
        return new Item(dto.getId(), null, dto.getName(), dto.getDescription(), dto.getAvailable(),
                null, null, null);
    }

    public ItemDto convert(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                null,
                null,
                null
        );
    }

    public ItemDto convert(Item item, Booking last, Booking next, List<CommentDto> comments) {
        ItemDto.BookingForItemDto lastBookingToAdd = null;
        ItemDto.BookingForItemDto nextBookingToAdd = null;

        if (last != null) {
            lastBookingToAdd = new ItemDto.BookingForItemDto(
                    last.getId(),
                    last.getStart().toLocalDateTime(),
                    last.getEnd().toLocalDateTime(),
                    last.getBooker().getId()
            );
        }

        if (next != null) {
            nextBookingToAdd = new ItemDto.BookingForItemDto(
                    next.getId(),
                    next.getStart().toLocalDateTime(),
                    next.getEnd().toLocalDateTime(),
                    next.getBooker().getId()
            );
        }
        
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                lastBookingToAdd,
                nextBookingToAdd,
                comments
        );
    }

    public Item update(Item item, ItemDto dto) {
        Item updated = new Item(item.getId(), item.getOwner(), item.getName(), item.getDescription(), item.getAvailable(),
                null, null, null);
//        if (dto.getUser() != null) {
//            updated.setUser(dto.getUser());
//        }
        if (dto.getName() != null) {
            updated.setName(dto.getName());
        }
        if (dto.getAvailable() != null) {
            updated.setAvailable(dto.getAvailable());
        }
        if (dto.getDescription() != null) {
            updated.setDescription(dto.getDescription());
        }
        return updated;
    }
}
