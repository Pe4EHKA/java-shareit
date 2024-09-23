package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Collection<Item> getItemsByOwnerId(Long ownerId);

    Collection<Item> getItemsByText(String text);

    ItemDto getItemById(Long itemId);

    ItemDto addItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long ownerId, Long itemId);
}
