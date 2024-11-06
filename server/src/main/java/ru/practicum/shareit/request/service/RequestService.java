package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.Collection;

public interface RequestService {
    RequestDto addRequest(Long userId, ItemRequestDto requestDto);

    Collection<RequestDto> getUserRequests(Long userId);

    Collection<RequestDto> getAllRequests();

    RequestDto getRequest(Long requestId);
}
