package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepositoryJPA;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepositoryJPA;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepositoryJPA itemRepository;
    private final UserRepositoryJPA userRepository;

    @Override
    public Collection<ItemDto> getItemsByOwnerId(Long ownerId) {
        return itemRepository.findAllByOwnerId(ownerId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public Collection<ItemDto> getItemsByText(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findAllByText(text).stream()
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return ItemMapper.toItemDto(itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item with id " + itemId + " not found")));
    }

    @Override
    public ItemDto addItem(ItemCreateDto itemDto, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User with id " + ownerId + " not found"));
        Item item = ItemMapper.toItem(itemDto, owner);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto itemDto, Long ownerId, Long itemId) {
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
        if (!oldItem.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Item with id " + itemId + " is not owned by owner with id " + ownerId);
        }

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            oldItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            oldItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(oldItem));
    }
}
