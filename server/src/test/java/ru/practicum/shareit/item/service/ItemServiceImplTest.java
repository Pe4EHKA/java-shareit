package ru.practicum.shareit.item.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class ItemServiceImplTest {

    private static final long USER_FIRST_ID = 1L;
    private static final long USER_SECOND_ID = 2L;
    private static final long USER_THIRD_ID = 3L;

    private static final long REQUEST_FIRST_ID = 1L;

    private static final long ITEM_FIRST_ID = 1L;
    private static final long ITEM_SECOND_ID = 2L;

    private static final long BOOKING_SECOND_ID = 2L;
    private static final long BOOKING_FOURTH_ID = 4L;

    private static final long COMMENT_FIRST_ID = 1L;

    private final EntityManager em;
    private final ItemService itemService;

    private User userFirst;
    private User userSecond;
    private User userThird;

    private ItemRequest request;

    private Item itemFirst;
    private Item itemSecond;

    private Booking bookingSecond;
    private Booking bookingFourth;

    private Comment commentFirst;

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

        userThird = new User();
        userThird.setId(USER_THIRD_ID);
        userThird.setName("userThird");
        userThird.setEmail("userThird@mail.ru");

        request = new ItemRequest();
        request.setId(REQUEST_FIRST_ID);
        request.setDescription("needSecondItem");
        request.setCreated(LocalDateTime.of(2024, 1, 1, 10, 0, 0));
        request.setRequester(userSecond);

        itemFirst = new Item();
        itemFirst.setId(ITEM_FIRST_ID);
        itemFirst.setName("itemFirst");
        itemFirst.setDescription("itemFirstDescription");
        itemFirst.setAvailable(true);
        itemFirst.setOwner(userFirst);

        itemSecond = new Item();
        itemSecond.setId(ITEM_SECOND_ID);
        itemSecond.setName("itemSecond");
        itemSecond.setDescription("itemSecondDescription");
        itemSecond.setAvailable(false);
        itemSecond.setOwner(userFirst);
        itemSecond.setRequest(request);

        bookingSecond = new Booking();
        bookingSecond.setId(BOOKING_SECOND_ID);
        bookingSecond.setStart(LocalDateTime.of(2024, 1, 1, 11, 0, 0));
        bookingSecond.setEnd(LocalDateTime.of(2024, 1, 1, 11, 10, 0));
        bookingSecond.setItem(itemFirst);
        bookingSecond.setBooker(userThird);
        bookingSecond.setStatus(BookingStatus.APPROVED);

        bookingFourth = new Booking();
        bookingFourth.setId(BOOKING_FOURTH_ID);
        bookingFourth.setStart(LocalDateTime.of(2075, 1, 1, 11, 0, 0));
        bookingFourth.setEnd(LocalDateTime.of(2075, 1, 1, 12, 0, 0));
        bookingFourth.setItem(itemFirst);
        bookingFourth.setBooker(userThird);
        bookingFourth.setStatus(BookingStatus.APPROVED);

        commentFirst = new Comment();
        commentFirst.setId(COMMENT_FIRST_ID);
        commentFirst.setText("niceItem");
        commentFirst.setItem(itemFirst);
        commentFirst.setAuthor(userThird);
        commentFirst.setCreated(LocalDateTime.of(2024, 1, 1, 11, 50, 0));
    }

    @Test
    @DisplayName("Сервис предмета: получение предметов по id владельца")
    void shouldGetItemsByOwnerId() {
        Collection<ItemWithBookingsDto> items = itemService.getItemsByOwnerId(USER_FIRST_ID);

        assertEquals(List.of(ItemMapper.toItemWithBookingsDto(itemFirst, bookingSecond.getEnd(),
                        bookingFourth.getStart(), List.of(ItemMapper.toCommentDto(commentFirst))),
                ItemMapper.toItemWithBookingsDto(itemSecond, null,
                        null, List.of())), items);
    }

    @Test
    @DisplayName("Сервис предмета: получение предмета по названию/описанию")
    void shouldGetItemsByText() {
        Collection<ItemDto> items = itemService.getItemsByText("item");

        assertEquals(List.of(ItemMapper.toItemDto(itemFirst)), items);
    }

    @Test
    @DisplayName("Сервис предмета: получение предмета по id")
    void shouldGetItemById() {
        ItemWithBookingsDto item = itemService.getItemById(1L);

        assertEquals(ItemMapper.toItemWithBookingsDto(itemFirst, bookingSecond.getEnd(), bookingFourth.getStart(),
                List.of(ItemMapper.toCommentDto(commentFirst))), item);
    }

    @Test
    @DisplayName("Сервис предмета: добавление предмета")
    void shouldAddItem() {
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("itemCreate");
        itemCreateDto.setDescription("itemCreateDescription");
        itemCreateDto.setAvailable(true);

        itemService.addItem(itemCreateDto, USER_FIRST_ID);

        TypedQuery<Item> query = em.createQuery("SELECT it FROM Item it WHERE it.name = :name", Item.class);
        query.setParameter("name", itemCreateDto.getName());

        Item result = query.getSingleResult();

        assertEquals(itemCreateDto.getName(), result.getName());
        assertEquals(itemCreateDto.getDescription(), result.getDescription());
        assertEquals(itemCreateDto.getAvailable(), result.getAvailable());
    }

    @Test
    @DisplayName("Сервис предмета: обновление предмета")
    void shouldUpdateItem() {
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("itemUpdate");
        itemUpdateDto.setDescription("itemUpdateDescription");
        itemUpdateDto.setAvailable(true);

        itemService.updateItem(itemUpdateDto, USER_FIRST_ID, ITEM_FIRST_ID);

        TypedQuery<Item> query = em.createQuery("SELECT it FROM Item it WHERE it.id = :id", Item.class);
        query.setParameter("id", ITEM_FIRST_ID);

        Item result = query.getSingleResult();

        assertEquals(itemUpdateDto.getName(), result.getName());
        assertEquals(itemUpdateDto.getDescription(), result.getDescription());
        assertEquals(itemUpdateDto.getAvailable(), result.getAvailable());
    }

    @Test
    @DisplayName("Сервис предмета: добавление комментария")
    void shouldAddComment() {
        CommentCreateDto commentCreateDto = new CommentCreateDto();
        commentCreateDto.setText("greatItem");

        itemService.addComment(ITEM_FIRST_ID, USER_THIRD_ID, commentCreateDto.getText());

        TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c WHERE c.text = :text", Comment.class);
        query.setParameter("text", commentCreateDto.getText());

        Comment result = query.getSingleResult();

        assertEquals(commentCreateDto.getText(), result.getText());
    }

    @Test
    @DisplayName("Exception: User not found")
    void shouldThrowNotFoundExceptionWhenUserNotFound() {
        assertThrows(NotFoundException.class, () -> {
            itemService.getItemsByOwnerId(999L);
        });
    }

    @Test
    @DisplayName("Exception: Item not found")
    void shouldThrowNotFoundExceptionWhenItemNotFound() {
        assertThrows(NotFoundException.class, () -> {
            itemService.getItemById(999L);
        });
    }

    @Test
    @DisplayName("Exception: Request not found when adding item")
    void shouldThrowNotFoundExceptionWhenRequestNotFound() {
        ItemCreateDto itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("Test Item");
        itemCreateDto.setDescription("Description");
        itemCreateDto.setAvailable(true);
        itemCreateDto.setRequestId(999L);

        assertThrows(NotFoundException.class, () -> {
            itemService.addItem(itemCreateDto, USER_FIRST_ID);
        });
    }

    @Test
    @DisplayName("Exception: User not the owner when updating item")
    void shouldThrowNotFoundExceptionWhenUserNotOwner() {
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("Updated Name");

        assertThrows(NotFoundException.class, () -> {
            itemService.updateItem(itemUpdateDto, USER_SECOND_ID, ITEM_FIRST_ID);
        });
    }

    @Test
    @DisplayName("Exception: Comment without completed booking")
    void shouldThrowNotAvailableExceptionWhenNoCompletedBookingForComment() {
        assertThrows(NotAvailableException.class, () -> {
            itemService.addComment(ITEM_FIRST_ID, USER_SECOND_ID, "This item is great!");
        });
    }
}