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

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<Item> getItemsByOwnerId(Long ownerId) {
        return itemRepository.findAllByOwner(ownerId);
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
    public ItemDto getItemById(Long itemId) {
        return ItemMapper.toItemDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item with id " + itemId + " not found")));
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " not found"));
        Item item = ItemMapper.toItem(itemDto, ownerId);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long ownerId, Long itemId) {
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
        if (!oldItem.getOwner().equals(ownerId)) {
            throw new NotFoundException("Item with id " + itemId + " is not owned by owner with id " + ownerId);
        }
        Item newItem = ItemMapper.toItem(itemDto, ownerId);
        newItem.setId(oldItem.getId());
        return ItemMapper.toItemDto(itemRepository.update(newItem));
    }
}
