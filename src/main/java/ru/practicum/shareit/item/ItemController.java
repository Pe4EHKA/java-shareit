package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto itemDto,
                           @RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        ItemDto item = itemService.addItem(itemDto, ownerId);
        log.info("Item added: {}", item);
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @RequestHeader(Headers.SHARER_USER_ID) Long ownerId,
                              @PathVariable("itemId") Long itemId) {
        ItemDto item = itemService.updateItem(itemDto, ownerId, itemId);
        log.info("Item updated: {}", item);
        return item;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId,
                           @RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        ItemDto item = itemService.getItemById(itemId);
        log.info("Item found: {}", item);
        return item;
    }

    @GetMapping
    public Collection<Item> getUserItems(@RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        Collection<Item> userItems = itemService.getItemsByOwnerId(ownerId);
        log.info("User items found: {}", userItems);
        return userItems;
    }

    @GetMapping("/search")
    public Collection<Item> getItemsByText(@RequestParam("text") String text,
                                           @RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        Collection<Item> textItems = itemService.getItemsByText(text);
        log.info("Searching for items with text {}", text);
        return textItems;
    }
}
