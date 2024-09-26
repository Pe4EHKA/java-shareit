package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Get all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long userId) {
        log.info("Get user by id: {}", userId);
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        userDto = userService.addUser(userDto);
        log.info("Add user: {}", userDto);
        return userDto;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto,
                              @PathVariable("userId") Long userId) {
        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long userId) {
        log.info("Delete user by id: {}", userId);
        userService.deleteUser(userId);
    }
}
