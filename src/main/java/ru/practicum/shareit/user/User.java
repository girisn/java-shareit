package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.common.Model;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends Model<Integer> {
    private String email;
    private String name;

    public User(Integer id, String email, String name) {
        super(id);
        this.email = email;
        this.name = name;
    }
}
