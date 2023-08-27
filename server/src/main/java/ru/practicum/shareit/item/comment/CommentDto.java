package ru.practicum.shareit.item.comment;

import lombok.*;
import ru.practicum.shareit.marker.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    @NotBlank
    @Size(max = 1000, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String text;

    private Long itemId;

    @Size(max = 255, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String authorName;

    private LocalDateTime created;
}
