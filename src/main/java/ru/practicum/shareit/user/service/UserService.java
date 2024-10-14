package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAllUsers();

    UserDto getUserById(Long userId);

    UserDto addUser(UserCreateDto userDto);

    UserDto updateUser(UserUpdateDto userDto, Long userId);

    void deleteUser(Long userId);
}
