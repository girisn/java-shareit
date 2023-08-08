package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper mapper,
                       UserRepository repository) {
        this.userMapper = mapper;
        this.userRepository = repository;
    }

    public List<UserDto> findAll() {
        return this.userRepository.findAll().stream().map(this.userMapper::convert).collect(Collectors.toList());
    }

    public UserDto findById(Long id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return this.userMapper.convert(user);
    }

    public UserDto save(UserDto userDto) {
        User user = this.userMapper.convert(userDto);
        validate(user);
        return this.userMapper.convert(this.userRepository.save(user));
    }

    public UserDto patch(Long userId, UserDto userDto) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User updated = this.userMapper.update(user, userDto);
        validate(user);
        return this.userMapper.convert(this.userRepository.save(updated));
    }

    public void remove(Long userId) {
        this.userRepository.deleteById(userId);
    }

    private void validate(User user) {
        if (user.getEmail() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
//        Optional<User> userOpt = userRepository.findByEmail(user.getEmail());
//        if (userOpt.isPresent() && !userOpt.get().getId().equals(user.getId())) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT);
//        }
    }
}
