package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();

    User getUserById(Long id);

    User addUser(User user);

    User updateUser(User user, Long id);

    void deleteUser(Long id);
}
