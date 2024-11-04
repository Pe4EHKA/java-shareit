package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class RequestServiceImplTest {
    private static final long USER_FIRST_ID = 1L;
    private static final long USER_SECOND_ID = 2L;

    private static final long REQUEST_FIRST_ID = 1L;

    private static final long ITEM_SECOND_ID = 2L;

    private final RequestService requestService;
    private final EntityManager em;

    private User userFirst;
    private User userSecond;

    private ItemRequest request;

    private Item itemSecond;

    @BeforeEach
    void setUp() {
        userFirst = new User();
        userFirst.setId(USER_FIRST_ID);
        userFirst.setName("userFirst");
        userFirst.setEmail("userFirst@mail.ru");

        userSecond = new User();
        userSecond.setId(USER_SECOND_ID);
        userSecond.setName("userSecond");
        userSecond.setEmail("userSecond@mail.ru");

        request = new ItemRequest();
        request.setId(REQUEST_FIRST_ID);
        request.setDescription("needSecondItem");
        request.setCreated(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
        request.setRequester(userSecond);

        itemSecond = new Item();
        itemSecond.setId(ITEM_SECOND_ID);
        itemSecond.setName("itemSecond");
        itemSecond.setDescription("itemSecondDescription");
        itemSecond.setAvailable(false);
        itemSecond.setOwner(userFirst);
        itemSecond.setRequest(request);
    }

    @Test
    @DisplayName("Сервис запросов: добавление запроса")
    void shouldAddRequest() {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("needThirdItem");

        requestService.addRequest(USER_FIRST_ID, requestDto);

        TypedQuery<ItemRequest> query = em.createQuery("SELECT ir FROM ItemRequest ir " +
                "WHERE ir.description = :desc", ItemRequest.class);
        query.setParameter("desc", "needThirdItem");

        ItemRequest result = query.getSingleResult();

        assertEquals(requestDto.getDescription(), result.getDescription());
        assertEquals(USER_FIRST_ID, result.getRequester().getId());
    }

    @Test
    @DisplayName("Сервис запросов: получение запросов пользователя")
    void shouldGetUserRequests() {
        Collection<RequestDto> requestDtos = requestService.getUserRequests(USER_SECOND_ID);

        assertEquals(List.of(RequestMapper
                .toRequestDto(request, List.of(ItemMapper.toItemDto(itemSecond)))), requestDtos);
    }

    @Test
    @DisplayName("Сервис запросов: получение всех запросов")
    void shouldGetAllRequests() {
        Collection<RequestDto> requestDtos = requestService.getAllRequests();

        assertEquals(List.of(RequestMapper.toRequestDto(request, List.of())), requestDtos);
    }

    @Test
    @DisplayName("Сервис запросов: получить запрос")
    void getRequest() {
        RequestDto requestDto = requestService.getRequest(REQUEST_FIRST_ID);

        assertEquals(RequestMapper.toRequestDto(request, List.of(ItemMapper.toItemDto(itemSecond))), requestDto);
    }
}