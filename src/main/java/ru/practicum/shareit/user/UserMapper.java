package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@UtilityClass
public class UserMapper {
    public UserDto userToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public User userDtotoUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public ItemDto.Owner userDtoToOwner(UserDto userDto) {
        return new ItemDto.Owner(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }
}
