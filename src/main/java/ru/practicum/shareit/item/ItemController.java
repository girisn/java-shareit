package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable Integer itemId) {
        return itemService.findById(itemId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Integer userId,
                          @RequestBody ItemDto item) {
        return itemService.save(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Integer userId,
                          @PathVariable Integer itemId,
                          @RequestBody ItemDto item) {
        return itemService.patch(userId, itemId, item);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                @RequestParam("text") String search) {
        return itemService.search(search);
    }
}
