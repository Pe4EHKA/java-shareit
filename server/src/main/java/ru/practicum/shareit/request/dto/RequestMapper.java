package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestMapper {

    public static RequestDto toRequestDto(ItemRequest request, List<ItemDto> items) {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
        requestDto.setCreated(request.getCreated());
        requestDto.setUser(UserMapper.toUserDto(request.getRequester()));
        requestDto.setItems(items);

        return requestDto;
    }

    public static ItemRequest toItemRequest(ItemRequestDto requestDto, User requester) {
        ItemRequest request = new ItemRequest();
        request.setDescription(requestDto.getDescription());
        request.setCreated(LocalDateTime.now());
        request.setRequester(requester);

        return request;
    }

    public static Collection<RequestDto> toRequestDtos(List<ItemRequest> itemRequests, List<ItemDto> items) {
        Map<Long, List<ItemDto>> itemsByRequestId = items.stream()
                .collect(Collectors.groupingBy(ItemDto::getRequestId));

        return itemRequests.stream()
                .map(itemRequest -> {
                    List<ItemDto> requestItems = itemsByRequestId.getOrDefault(itemRequest.getId(), List.of());
                    return toRequestDto(itemRequest, requestItems);
                })
                .toList();
    }
}
