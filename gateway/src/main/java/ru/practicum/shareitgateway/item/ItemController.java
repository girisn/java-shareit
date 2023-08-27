package ru.practicum.shareitgateway.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.item.dto.CommentDto;
import ru.practicum.shareitgateway.item.dto.ItemDto;
import ru.practicum.shareitgateway.util.Marker;

import javax.validation.Valid;

import static ru.practicum.shareitgateway.util.Constants.REQUEST_HEADER_USER_ID;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                             @Validated(Marker.OnCreate.class) @Valid @RequestBody ItemDto requestDto) {
        return itemClient.createItem(userId, requestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable Long id,
                                              @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        return itemClient.getItemById(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        return itemClient.getItems(userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@Validated(Marker.OnUpdate.class) @RequestBody ItemDto requestDto,
                                             @PathVariable Long id,
                                             @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        return itemClient.updateItem(requestDto, id, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemByText(@RequestParam String text,
                                                   @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        return itemClient.searchItemByText(userId, text);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeItem(@PathVariable Long id) {
        return itemClient.removeItem(id);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long itemId,
                                                @RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                @RequestBody CommentDto requestDto) {
        return itemClient.createComment(itemId, userId, requestDto);
    }
}
