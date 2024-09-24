package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.*;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> claimedEmail = new HashSet<>();
    private long seq = 0;

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Collection<String> findEmails() {
        return claimedEmail;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User save(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        claimedEmail.add(user.getEmail());
        return users.get(user.getId());
    }

    @Override
    public User update(User user) {
        User updatedUser = users.get(user.getId());
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            claimedEmail.remove(updatedUser.getEmail());
            updatedUser.setEmail(user.getEmail());
            claimedEmail.add(updatedUser.getEmail());
        }
        return updatedUser;
    }

    @Override
    public void delete(long id) {
        claimedEmail.remove(users.get(id).getEmail());
        users.remove(id);
    }

    private long generateId() {
        return ++seq;
    }
}
