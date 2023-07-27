package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.DtoMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper implements DtoMapper<Item, ItemDto> {
    public Item convert(ItemDto dto) {
        return new Item(dto.getId(), dto.getUserId(), dto.getName(), dto.getDescription(), dto.getAvailable());
    }

    public ItemDto convert(Item item) {
        return new ItemDto(item.getId(), item.getUserId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public Item update(Item item, ItemDto dto) {
        Item updated = new Item(item.getId(), item.getUserId(), item.getName(), item.getDescription(), item.getAvailable());
        if (dto.getUserId() != null) {
            updated.setUserId(dto.getUserId());
        }
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
