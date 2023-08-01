package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.common.CrudService;

import java.util.Optional;

@Service
public class UserService extends CrudService<Long, User, UserDto> {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserMapper mapper,
                       UserRepository repository) {
        super(mapper, repository);
        userRepository = repository;
    }

    @Override
    protected Void validate(User user) {
        if (user.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<User> userOpt = userRepository.findByEmail(user.getEmail());
        if (userOpt.isPresent() && !userOpt.get().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return null;
    }
}
