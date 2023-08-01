package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.DtoMapper;

@Component
public class ItemMapper implements DtoMapper<Item, ItemDto> {
    public Item convert(ItemDto dto) {
        return new Item(dto.getId(), null, dto.getName(), dto.getDescription(), dto.getAvailable());
    }

    public ItemDto convert(Item item) {
        return new ItemDto(item.getId(), item.getUser().getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public Item update(Item item, ItemDto dto) {
        Item updated = new Item(item.getId(), item.getUser(), item.getName(), item.getDescription(), item.getAvailable());
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
