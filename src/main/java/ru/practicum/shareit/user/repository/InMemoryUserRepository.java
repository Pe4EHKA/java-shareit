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
    public User update(User updatedUser) {
        User user = users.get(updatedUser.getId());
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            claimedEmail.remove(user.getEmail());
            user.setEmail(updatedUser.getEmail());
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
