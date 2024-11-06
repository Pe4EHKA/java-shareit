package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        ResponseEntity<Object> response = userClient.getAllUsers();
        log.info("Get all users: {}", response.getBody());
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Long userParamId) {
        log.info("Get user by id: {}", userParamId);
        ResponseEntity<Object> response = userClient.getUserById(userParamId);
        log.info("Get user by id: {}", response.getBody());
        return response;
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserCreateDto userDto) {
        log.info("Add user: {}", userDto);
        ResponseEntity<Object> userDtoCreated = userClient.addUser(userDto);
        log.info("Add user: {}", userDtoCreated.getBody());
        return userDtoCreated;
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UserUpdateDto userUpdateDto,
                                             @PathVariable("userId") Long userUpdateId) {
        log.info("Update user: {}", userUpdateDto);
        ResponseEntity<Object> userDtoUpdated = userClient.updateUser(userUpdateDto, userUpdateId);
        log.info("Update user: {}", userDtoUpdated.getBody());
        return userDtoUpdated;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long userParamId) {
        log.info("Delete user with id: {}", userParamId);
        ResponseEntity<Object> userDtoDeleted = userClient.deleteUser(userParamId);
        log.info("Deleted user by id: {}", userParamId);
        return userDtoDeleted;
    }
}
