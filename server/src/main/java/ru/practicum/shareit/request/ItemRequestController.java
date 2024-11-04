package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.Headers;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.Collection;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestDto addRequest(@RequestHeader(Headers.SHARER_USER_ID) Long userId,
                                 @RequestBody ItemRequestDto requestDto) {
        log.info("Add request: {}, userId: {}", requestDto, userId);
        RequestDto requestDtoAnswer = requestService.addRequest(userId, requestDto);
        log.info("Added request: {}", requestDtoAnswer);
        return requestDtoAnswer;
    }

    @GetMapping
    public Collection<RequestDto> getUserRequests(@RequestHeader(Headers.SHARER_USER_ID) Long userId) {
        log.info("Get requests for user with id: {}", userId);
        Collection<RequestDto> requestDtoAnswer = requestService.getUserRequests(userId);
        log.info("Got requests for user: {}", requestDtoAnswer);
        return requestDtoAnswer;
    }

    @GetMapping("/all")
    public Collection<RequestDto> getAllRequests(@RequestHeader(Headers.SHARER_USER_ID) Long userId) {
        log.info("Get all requests for user with id: {}", userId);
        Collection<RequestDto> requestDtos = requestService.getAllRequests();
        log.info("Got all requests {}, for user with id: {}", requestDtos, userId);
        return requestDtos;
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequest(@RequestHeader(Headers.SHARER_USER_ID) Long userId,
                                 @PathVariable("requestId") Long requestId) {
        log.info("Get request with id: {}", requestId);
        RequestDto requestDto = requestService.getRequest(requestId);
        log.info("Got request with id: {}", requestDto);
        return requestDto;
    }
}
