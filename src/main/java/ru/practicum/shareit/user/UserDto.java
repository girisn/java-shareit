package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.common.ModelDto;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends ModelDto<Integer> {
    @Email
    private String email;
    private String name;

    public UserDto(Integer id, String email, String name) {
        super(id);
        this.email = email;
        this.name = name;
    }
}
