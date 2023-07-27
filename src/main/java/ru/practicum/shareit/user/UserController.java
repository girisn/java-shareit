package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Integer userId) {
        return userService.findById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto user) {
        return userService.save(user);
    }

    @PatchMapping("/{userId}")
    public UserDto patch(@PathVariable Integer userId,
                         @Valid @RequestBody UserDto user) {
        return userService.patch(userId, user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer userId) {
        userService.remove(userId);
    }
}
