package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;
import java.time.Instant;

@Component
public class CommentMapper {
    public Comment convert(User user, Item item, CommentDto dto) {
        return new Comment(null, dto.getText(), user, Timestamp.from(Instant.now()), item);
    }

    public CommentDto convert(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated().toLocalDateTime());
    }
}
