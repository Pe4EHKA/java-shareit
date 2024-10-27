package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@Valid @RequestBody ItemCreateDto requestDto,
                                  @RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        log.info("Adding new item: {}", requestDto);
        ResponseEntity<Object> item = itemClient.addItem(requestDto, ownerId);
        log.info("Item added: {}", item.getBody());
        return item;
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemUpdateDto itemDto,
                                             @RequestHeader(Headers.SHARER_USER_ID) Long ownerId,
                                             @PathVariable("itemId") Long itemId) {
        log.info("Updating item: {}", itemDto);
        ResponseEntity<Object> item = itemClient.updateItem(itemDto, ownerId, itemId);
        log.info("Item updated: {}", item.getBody());
        return item;
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId,
                                       @RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        log.info("Getting item with id: {}", itemId);
        ResponseEntity<Object> item = itemClient.getItemById(itemId, ownerId);
        log.info("Item found: {}", item.getBody());
        return item;
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(Headers.SHARER_USER_ID) Long ownerId) {
        log.info("Getting user items");
        ResponseEntity<Object> userItems = itemClient.getItemsByOwnerId(ownerId);
        log.info("User items found: {}", userItems.getBody());
        return userItems;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByText(@RequestParam("text") String text,
                                              @RequestHeader(Headers.SHARER_USER_ID) Long userId) {
        log.info("Getting items by text: {}", text);
        ResponseEntity<Object> textItems = itemClient.getItemsByText(text, userId);
        log.info("Searching for items with text {}", textItems.getBody());
        return textItems;
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable(name = "itemId") Long itemId,
                                 @Valid @RequestBody CommentCreateDto commentCreateDto,
                                 @RequestHeader(Headers.SHARER_USER_ID) Long userId) {
        log.info("Adding comment: {} to item with id: {}", commentCreateDto, itemId);
        ResponseEntity<Object> commentDto = itemClient.addComment(itemId, userId, commentCreateDto);
        log.info("Comment added: {}", commentDto.getBody());
        return commentDto;
    }
}
