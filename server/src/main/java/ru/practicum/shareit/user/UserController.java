package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("Get all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable("id") Long userId) {
        log.info("Get user by id: {}", userId);
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDto addUser(@RequestBody UserCreateDto userDto) {
        UserDto userDtoCreated = userService.addUser(userDto);
        log.info("Add user: {}", userDtoCreated);
        return userDtoCreated;
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserUpdateDto userDto,
                              @PathVariable("userId") Long userId) {
        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long userId) {
        log.info("Delete user by id: {}", userId);
        userService.deleteUser(userId);
    }
}
