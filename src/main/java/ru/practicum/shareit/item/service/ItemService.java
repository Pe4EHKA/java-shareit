package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> getItemsByOwnerId(Long ownerId);

    Collection<ItemDto> getItemsByText(String text);

    ItemDto getItemById(Long itemId);

    ItemDto addItem(ItemCreateDto itemDto, Long ownerId);

    ItemDto updateItem(ItemUpdateDto itemDto, Long ownerId, Long itemId);
}
