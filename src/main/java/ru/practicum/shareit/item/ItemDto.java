package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private Boolean available;

    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;
    @ToString.Exclude
    List<CommentDto> comments;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingForItemDto {
        private Long id;
        private LocalDateTime start;
        private LocalDateTime end;
        private Long bookerId;
    }
}
