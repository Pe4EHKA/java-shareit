package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    Collection<Item> findAll();

    Optional<Item> findById(Long id);

    Item save(Item item);

    Item update(Item item);

    void delete(Item item);
}
