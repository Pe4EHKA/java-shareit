package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
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
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
    }

    @Override
    public User addUser(User user) {
        if (checkEmailAlreadyExists(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, Long id) {
        if (checkEmailAlreadyExists(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }
        User oldUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));
        user.setId(oldUser.getId());
        return userRepository.update(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    private boolean checkEmailAlreadyExists(String email) {
        Collection<User> users = userRepository.findAll();
        return users.stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }
}
