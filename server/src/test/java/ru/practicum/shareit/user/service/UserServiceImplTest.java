package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class UserServiceImplTest {
    private static final long USER_FIRST_ID = 1L;
    private static final long USER_SECOND_ID = 2L;
    private static final long USER_THIRD_ID = 3L;

    private final EntityManager em;
    private final UserService userService;

    private User userFirst;
    private User userSecond;
    private User userThird;

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
    }

    @Test
    @DisplayName("Сервис пользователя: получение всех пользователей")
    void shouldGetAllUsers() {
        Collection<UserDto> users = userService.getAllUsers();

        assertEquals(Stream.of(userFirst, userSecond, userThird)
                .map(UserMapper::toUserDto)
                .toList(), users);
    }

    @Test
    @DisplayName("Сервис пользователя: получения пользователя по id")
    void shouldGetUserById() {
        UserDto userDto = userService.getUserById(USER_FIRST_ID);

        assertEquals(UserMapper.toUserDto(userFirst), userDto);
    }

    @Test
    @DisplayName("Сервис пользователя: добавление пользователя")
    void shouldAddUser() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("newUser");
        userCreateDto.setEmail("newUser@mail.ru");

        userService.addUser(userCreateDto);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", "newUser@mail.ru");

        User user = query.getSingleResult();

        assertEquals(userCreateDto.getName(), user.getName());
        assertEquals(userCreateDto.getEmail(), user.getEmail());
    }

    @Test
    @DisplayName("Сервис пользователя: обновление пользователя")
    void shouldUpdateUser() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(USER_SECOND_ID);
        userUpdateDto.setName("newUser");
        userUpdateDto.setEmail("newUser@mail.ru");

        userService.updateUser(userUpdateDto, userUpdateDto.getId());

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", "newUser@mail.ru");

        User user = query.getSingleResult();

        assertEquals(userUpdateDto.getId(), user.getId());
        assertEquals(userUpdateDto.getName(), user.getName());
        assertEquals(userUpdateDto.getEmail(), user.getEmail());
    }

    @Test
    @DisplayName("Сервис пользователя: удаление пользователя")
    void shouldDeleteUser() {
        userService.deleteUser(USER_FIRST_ID);

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
        query.setParameter("id", USER_FIRST_ID);

        List<User> users = query.getResultList();

        assertTrue(users.isEmpty());
    }
}