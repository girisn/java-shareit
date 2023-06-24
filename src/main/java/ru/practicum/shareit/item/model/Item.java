package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.common.Model;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item extends Model<Integer> {
    private Integer userId;
    private String name;
    private String description;
    private Boolean available;

    public Item(Integer id, Integer userId, String name, String description, Boolean available) {
        super(id);
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
