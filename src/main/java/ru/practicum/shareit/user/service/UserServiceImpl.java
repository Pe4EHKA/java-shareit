package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.repository.UserRepositoryJPA;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepositoryJPA userRepositoryJPA;

    @Override
    public Collection<UserDto> getAllUsers() {
        return userRepositoryJPA.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepositoryJPA.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto addUser(UserCreateDto userDto) {
        if (userRepositoryJPA.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email " + userDto.getEmail() + " already exists");
        }
        User user = userRepositoryJPA.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserUpdateDto userDto, Long userId) {
        if (userRepositoryJPA.findByEmail(userDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email " + userDto.getEmail() + " already exists");
        }
        User oldUser = userRepositoryJPA.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            oldUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            oldUser.setEmail(userDto.getEmail());
        }
        User user = userRepositoryJPA.save(oldUser);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepositoryJPA.deleteById(userId);
    }
}
