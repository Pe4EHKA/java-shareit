package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Collection<User> findAll();

    User findByEmail(String email);

    Optional<User> findById(long id);

    User save(User user);

    User update(User user);

    void delete(long id);
}
