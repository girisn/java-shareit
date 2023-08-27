package ru.practicum.shareitgateway.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareitgateway.request.dto.ItemRequestDto;
import ru.practicum.shareitgateway.util.Marker;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareitgateway.util.Constants.REQUEST_HEADER_USER_ID;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                    @Validated(Marker.OnCreate.class) @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByUser(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        return itemRequestClient.getItemRequestsByUser(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                 @PathVariable Long requestId) {
        return itemRequestClient.getItemRequest(requestId, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                     @PositiveOrZero
                                                     @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemRequestClient.getAllItemRequest(userId, from, size);
    }


}
