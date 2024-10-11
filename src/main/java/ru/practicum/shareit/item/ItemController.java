package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemCreateDto itemDto,
                           @RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        ItemDto item = itemService.addItem(itemDto, ownerId);
        log.info("Item added: {}", item);
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemUpdateDto itemDto,
                              @RequestHeader(Headers.SHARER_USER_ID) Long ownerId,
                              @PathVariable("itemId") Long itemId) {
        ItemDto item = itemService.updateItem(itemDto, ownerId, itemId);
        log.info("Item updated: {}", item);
        return item;
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingsDto getItem(@PathVariable Long itemId,
                                       @RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        ItemWithBookingsDto item = itemService.getItemById(itemId);
        log.info("Item found: {}", item);
        return item;
    }

    @GetMapping
    public Collection<ItemWithBookingsDto> getUserItems(@RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        Collection<ItemWithBookingsDto> userItems = itemService.getItemsByOwnerId(ownerId);
        log.info("User items found: {}", userItems);
        return userItems;
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemsByText(@RequestParam("text") String text,
                                              @RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        Collection<ItemDto> textItems = itemService.getItemsByText(text);
        log.info("Searching for items with text {}", text);
        return textItems;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable(name = "itemId") Long itemId,
                                 @RequestBody CommentCreateDto commentCreateDto,
                                 @RequestHeader(Headers.SHARER_USER_ID) Long userId) {
        CommentDto commentDto = itemService.addComment(itemId, userId, commentCreateDto.getText());
        log.info("Comment added: {}", commentDto);
        return commentDto;
    }
}
