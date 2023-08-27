package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;

import java.util.List;

import static ru.practicum.shareit.util.Constants.REQUEST_HEADER_USER_ID;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping()
    public ItemRequestDto createRequest(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                        @RequestBody ItemRequestShortDto itemRequestShortDto) {
        return requestService.createRequest(id, itemRequestShortDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                         @PathVariable Long requestId) {
        return requestService.getRequestById(id, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        return requestService.getAllRequests(id, from, size);
    }

    @GetMapping()
    public List<ItemRequestDto> getAllRequestsByRequester(@RequestHeader(REQUEST_HEADER_USER_ID) Long id,
                                                          @RequestParam(defaultValue = "0") Integer from,
                                                          @RequestParam(defaultValue = "10") Integer size) {
        return requestService.getAllRequestsByRequester(id, from, size);
    }

}
