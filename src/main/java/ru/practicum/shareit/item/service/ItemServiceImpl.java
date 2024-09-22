package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<Item> getItemsByOwnerId(Long ownerId) {
        return itemRepository.findAll().stream()
                .filter(item -> Objects.equals(item.getOwner(), ownerId))
                .toList();
    }

    @Override
    public Collection<Item> getItemsByText(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findAll().stream()
                .filter(item -> (item.getName() != null && item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription() != null
                        && item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .toList();
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item with id " + itemId + " not found"));
    }

    @Override
    public Item addItem(ItemDto itemDto, Long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " not found"));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(ownerId);
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(ItemDto itemDto, Long ownerId, Long itemId) {
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
        if (!oldItem.getOwner().equals(ownerId)) {
            throw new NotFoundException("Item with id " + itemId + " is not owned by owner with id " + ownerId);
        }
        Item newItem = ItemMapper.toItem(itemDto);
        newItem.setId(oldItem.getId());
        return itemRepository.update(newItem);
    }
}
