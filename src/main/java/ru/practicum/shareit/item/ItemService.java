package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper mapper;
    private final CommentMapper commentMapper;

    @Autowired
    public ItemService(ItemMapper mapper,
                       ItemRepository repository,
                       UserRepository userRepository,
                       CommentRepository commentRepository,
                       CommentMapper commentMapper,
                       BookingRepository bookingRepository) {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.itemRepository = repository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public List<ItemDto> findAll(Long userId) {
        List<Item> list = itemRepository.findAllByOwnerId(userId);
        return getItemDto(list);
    }

    public ItemDto findById(Long userId, Long id) {
        Item item = this.itemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Booking lastBooking = bookingRepository.findPastOwnerBookings(item.getId(), userId, Timestamp.from(Instant.now()))
                .stream()
                .filter(booking -> booking.getStatus().equals(Booking.Status.APPROVED))
                .max(Comparator.comparing(Booking::getEnd))
                .orElse(null);

        Booking nextBooking = bookingRepository.findFutureOwnerBookings(item.getId(), userId, Timestamp.from(Instant.now()))
                .stream()
                .filter(booking -> booking.getStatus().equals(Booking.Status.APPROVED))
                .min(Comparator.comparing(Booking::getStart))
                .orElse(null);

        List<CommentDto> commentsDto = commentDto(item);
        return this.mapper.convert(item, lastBooking, nextBooking, commentsDto);
    }

    public List<ItemDto> search(String search) {
        if (!StringUtils.hasText(search))
            return Collections.emptyList();
        return itemRepository.findByDescriptionContainingIgnoreCaseAndAvailable(search, true)
                .stream()
                .map(mapper::convert)
                .collect(Collectors.toList());
    }

    public ItemDto save(Long userId, ItemDto dto) {
        Item item = mapper.convert(dto);
        validate(item);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        item.setOwner(user);
        return mapper.convert(this.itemRepository.save(item), null, null, null);
    }

    public ItemDto patch(Long userId, Long dtoId, ItemDto dto) {
        Item item = itemRepository.findById(dtoId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        item = mapper.update(item, dto);
        validate(item);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        item.setOwner(user);
        Booking lastBooking = bookingLast(item);
        Booking nextBooking = bookingNext(item);
        List<CommentDto> commentsDto = commentDto(item);
        return mapper.convert(itemRepository.save(item), lastBooking, nextBooking, commentsDto);
    }

    public Booking bookingLast(Item item) {
        return bookingRepository.findAllByItemIdAndStartBeforeOrderByStartDesc(item.getId(), Timestamp.from(Instant.now()))
                .stream()
                .filter(booking -> booking.getStatus().equals(Booking.Status.APPROVED))
                .max(Comparator.comparing(Booking::getEnd))
                .orElse(null);
    }

    public List<CommentDto> commentDto(Item item) {
        return commentRepository.getAllByItemId(item.getId())
                .stream()
                .map(commentMapper::convert)
                .collect(Collectors.toList());
    }

    public Booking bookingNext(Item item) {
        return bookingRepository.findAllByItemIdAndStartAfterOrderByStartDesc(item.getId(), Timestamp.from(Instant.now()))
                .stream()
                .filter(booking -> booking.getStatus().equals(Booking.Status.APPROVED))
                .min(Comparator.comparing(Booking::getStart))
                .orElse(null);
    }

    public CommentDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        if (commentDto.getText() == null || commentDto.getText().equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Long bookingsCount = bookingRepository.countAllByItemIdAndBookerIdAndEndBefore(itemId, userId, Timestamp.from(Instant.now()));

        if (bookingsCount == null || bookingsCount == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Comment comment = commentMapper.convert(user, item, commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(Timestamp.from(Instant.now()));
        return commentMapper.convert(commentRepository.save(comment));
    }

    private List<ItemDto> getItemDto(List<Item> its) {
        List<ItemDto> list = new ArrayList<>();
        for (Item item : its) {
            Booking lastBooking = bookingLast(item);

            Booking nextBooking = bookingNext(item);

            List<CommentDto> commentsDto = commentDto(item);
            item.setLastBooking(lastBooking);
            item.setNextBooking(nextBooking);
            item.setComments(commentsDto);
            list.add(mapper.convert(item, lastBooking, nextBooking, commentsDto));
        }
        return list;
    }

    private void validate(Item model) {
        if (model.getAvailable() == null
                || !StringUtils.hasText(model.getName())
                || !StringUtils.hasText(model.getDescription())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
