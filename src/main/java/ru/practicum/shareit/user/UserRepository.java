package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.CrudRepository;

import java.util.Optional;

@Repository
public class UserRepository extends CrudRepository<Integer, User> {
    public UserRepository() {
        super(1, (id) -> id + 1);
    }

    public Optional<User> findByEmail(String email) {
        return this.storage.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }
}
