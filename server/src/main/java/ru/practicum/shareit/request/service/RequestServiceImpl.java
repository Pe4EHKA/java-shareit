package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;


    @Override
    public RequestDto addRequest(Long userId, ItemRequestDto requestDto) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));
        ItemRequest itemRequest = requestRepository.save(RequestMapper.toItemRequest(requestDto, requester));
        return RequestMapper.toRequestDto(itemRequest, Collections.emptyList());
    }

    @Override
    public Collection<RequestDto> getUserRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));
        List<ItemRequest> itemRequest = requestRepository.findByRequesterIdOrderByCreatedDesc(userId);
        List<Long> itemRequestIds = itemRequest.stream()
                .map(ItemRequest::getId)
                .toList();
        List<ItemDto> items = itemRepository.findByRequestIdIn(itemRequestIds).stream()
                .map(ItemMapper::toItemDto)
                .toList();
        return RequestMapper.toRequestDtos(itemRequest, items);
    }

    @Override
    public Collection<RequestDto> getAllRequests() {
        List<ItemRequest> itemRequests = requestRepository.findAllByOrderByCreatedDesc();
        return itemRequests.stream()
                .map(itemRequest -> RequestMapper.toRequestDto(itemRequest, List.of()))
                .toList();
    }

    @Override
    public RequestDto getRequest(Long requestId) {
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request with id " + requestId + " not found"));
        List<ItemDto> items = itemRepository.findByRequestId(requestId).stream()
                .map(ItemMapper::toItemDto)
                .toList();
        return RequestMapper.toRequestDto(itemRequest, items);
    }
}
