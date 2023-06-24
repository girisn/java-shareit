package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.CrudRepository;

@Repository
public class UserRepository extends CrudRepository<Integer, User> {
    public UserRepository() {
        super(1, (id) -> id + 1);
    }
}
