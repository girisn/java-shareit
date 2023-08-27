package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ItemShortDto {
    private Long id;
    private String name;
    private String description;

    @NotNull
    private Boolean available;
    private Long requestId;
}
