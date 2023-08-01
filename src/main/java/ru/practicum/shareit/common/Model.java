package ru.practicum.shareit.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface Model<E> {
    public E getId();
}
