package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        if (checkEmailAlreadyExists(userDto.getEmail())) {
            throw new IllegalArgumentException("User with email " + userDto.getEmail() + " already exists");
        }
        User user = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        if (checkEmailAlreadyExists(userDto.getEmail())) {
            throw new IllegalArgumentException("User with email " + userDto.getEmail() + " already exists");
        }
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));
        userDto.setId(oldUser.getId());
        User user = userRepository.update(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.delete(userId);
    }

    private boolean checkEmailAlreadyExists(String email) {
        return userRepository.findEmails().contains(email);
    }
}
