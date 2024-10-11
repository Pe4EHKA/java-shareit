package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepositoryJPA extends JpaRepository<Comment, Long> {
    List<Comment> getCommentsByItemId(Long itemId);
}
