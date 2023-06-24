package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.common.CrudService;

import java.util.Collection;

@Service
public class UserService extends CrudService<Integer, User, UserDto> {
    @Autowired
    public UserService(UserMapper mapper,
                       UserRepository repository) {
        super(mapper, repository);
    }

    @Override
    protected Void validate(User user) {
        Collection<User> values = this.repository.findAll();
        if (user.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (values.stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return null;
    }
}
