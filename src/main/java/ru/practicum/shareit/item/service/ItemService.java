package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Collection<Item> getItemsByOwnerId(Long ownerId);

    Collection<Item> getItemsByText(String text);

    Item getItemById(Long itemId);

    Item addItem(ItemDto itemDto, Long ownerId);

    Item updateItem(ItemDto itemDto, Long ownerId, Long itemId);
}
