package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<ItemWithBookingsDto> getItemsByOwnerId(Long ownerId) {
        Collection<Item> items = itemRepository.findAllByOwnerId(ownerId);

        return items.stream()
                .map(this::getItemWithBookingsDto)
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
    @Transactional(readOnly = true)
    public ItemWithBookingsDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
        return getItemWithBookingsDto(item);
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

    @Override
    public CommentDto addComment(Long itemId, Long userId, String text) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item with id " + itemId + " not found"));
        if (!bookingRepository.existsBookingByItem_IdAndBooker_Id(itemId, userId)) {
            throw new NotAvailableException("You can comment only on completed bookings");
        }
        Comment comment = CommentMapper.toComment(user, item, text);

        return ItemMapper.toCommentDto(commentRepository.save(comment));
    }

    private ItemWithBookingsDto getItemWithBookingsDto(Item item) {
        LocalDateTime lastBookingDate = bookingRepository.findLastBookingDateByItemId(item.getId());
        LocalDateTime nextBookingDate = bookingRepository.findNextBookingDateByItemId(item.getId());
        List<CommentDto> commentDtos = commentRepository.getCommentsByItemId(item.getId()).stream()
                .map(ItemMapper::toCommentDto)
                .toList();
        return ItemMapper.toItemWithBookingsDto(item, lastBookingDate, nextBookingDate, commentDtos);
    }
}
