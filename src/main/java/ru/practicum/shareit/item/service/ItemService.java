package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

public interface ItemService {
    Collection<ItemWithBookingsDto> getItemsByOwnerId(Long ownerId);

    Collection<ItemDto> getItemsByText(String text);

    ItemWithBookingsDto getItemById(Long itemId);

    ItemDto addItem(ItemCreateDto itemDto, Long ownerId);

    ItemDto updateItem(ItemUpdateDto itemDto, Long ownerId, Long itemId);

    CommentDto addComment(Long itemId, Long userId, String text);
}
