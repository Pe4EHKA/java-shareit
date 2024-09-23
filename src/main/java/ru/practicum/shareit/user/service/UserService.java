package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, Long userId);

    void deleteUser(Long userId);
}
