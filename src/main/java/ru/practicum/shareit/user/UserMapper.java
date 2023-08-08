package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User convert(UserDto dto) {
        if (dto == null) {
            return null;
        }
        return new User(dto.getId(), dto.getEmail(), dto.getName());
    }

    public UserDto convert(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

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
