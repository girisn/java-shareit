package ru.practicum.shareitgateway.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareitgateway.client.BaseClient;
import ru.practicum.shareitgateway.user.dto.UserRequestDto;


@Service
@Slf4j
public class UserClient extends BaseClient {
    @Autowired
    public UserClient(@Qualifier("users") RestTemplate restTemplate) {
        super(restTemplate);
    }

    public ResponseEntity<Object> createUser(UserRequestDto userRequestDto) {
        log.info("Create user  {}", userRequestDto);
        return post("", userRequestDto);
    }

    public ResponseEntity<Object> getUserById(Long userId) {
        log.info("Get user with id = {}", userId);
        return get("/" + userId);
    }

    public ResponseEntity<Object> updateUserById(Long userId, UserRequestDto userRequestDto) {
        log.info("Update user with id = {}", userId);
        return patch("/" + userId, userRequestDto);
    }

    public ResponseEntity<Object> getUsers() {
        log.info("Get all users");
        return get("");
    }

    public ResponseEntity<Object> removeUserById(Long userId) {
        log.info("Remove user with id = {}", userId);
        return delete("/" + userId);
    }
}
