package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.common.ModelDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto extends ModelDto<Integer> {
    @Null
    private Integer userId;

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private Boolean available;

    public ItemDto(Integer id, Integer userId, String name, String description, Boolean available) {
        super(id);
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
