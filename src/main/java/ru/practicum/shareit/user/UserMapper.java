package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.DtoMapper;

@Component
public class UserMapper implements DtoMapper<User, UserDto> {
    @Override
    public User convert(UserDto dto) {
        if (dto == null) {
            return null;
        }
        return new User(dto.getId(), dto.getEmail(), dto.getName());
    }

    @Override
    public UserDto convert(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    @Override
    public User update(User user, UserDto dto) {
        User updated = new User(user.getId(), user.getEmail(), user.getName());
        if (dto.getEmail() != null) {
            updated.setEmail(dto.getEmail());
        }
        if (dto.getName() != null) {
            updated.setName(dto.getName());
        }
        return updated;
    }
}
