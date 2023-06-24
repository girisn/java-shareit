package ru.practicum.shareit.common;

public interface DtoMapper<MODEL extends Model, DTO extends ModelDto> {
    MODEL convert(DTO dto);

    DTO convert(MODEL dto);

    MODEL update(MODEL user, DTO dto);
}
