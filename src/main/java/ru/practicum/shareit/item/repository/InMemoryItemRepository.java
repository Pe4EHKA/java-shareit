package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long seq = 0;

    @Override
    public Collection<Item> findAll() {
        return items.values();
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item save(Item item) {
        item.setId(generateId());
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item update(Item item) {
        Item updatedItem = items.get(item.getId());
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        return updatedItem;
    }

    @Override
    public void delete(Item item) {
        items.remove(item.getId());
    }

    private long generateId() {
        return ++seq;
    }
}
