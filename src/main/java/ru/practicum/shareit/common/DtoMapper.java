package ru.practicum.shareit.common;

public interface DtoMapper<M extends Model, D extends ModelDto> {
    M convert(D dto);

    D convert(M dto);

    M update(M user, D dto);
}
