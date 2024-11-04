package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.Headers;
import ru.practicum.shareit.request.dto.ItemRequestDto;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader(Headers.SHARER_USER_ID) Long userId,
                                     @RequestBody ItemRequestDto requestDto) {
        log.info("Add request: {}, userId: {}", requestDto, userId);
        ResponseEntity<Object> requestDtoAnswer = requestClient.addRequest(userId, requestDto);
        log.info("Added request: {}", requestDtoAnswer);
        return requestDtoAnswer;
    }

    @GetMapping
    public ResponseEntity<Object> getUserRequests(@RequestHeader(Headers.SHARER_USER_ID) Long userId) {
        log.info("Get requests for user with id: {}", userId);
        ResponseEntity<Object> requestDtoAnswer = requestClient.getUserRequests(userId);
        log.info("Got requests for user: {}", requestDtoAnswer);
        return requestDtoAnswer;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader(Headers.SHARER_USER_ID) Long userId) {
        log.info("Get all requests for user with id: {}", userId);
        ResponseEntity<Object> requestDtos = requestClient.getAllRequests(userId);
        log.info("Got all requests {}, for user with id: {}", requestDtos, userId);
        return requestDtos;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader(Headers.SHARER_USER_ID) Long userId,
                                 @PathVariable("requestId") Long requestId) {
        log.info("Get request with id: {}", requestId);
        ResponseEntity<Object> requestDto = requestClient.getRequest(userId, requestId);
        log.info("Got request with id: {}", requestDto);
        return requestDto;
    }
}