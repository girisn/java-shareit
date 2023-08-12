
package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;


    public ItemDto createItem(ItemShortDto itemShortDto, Long userId) {
        ItemDto itemDto = itemMapper.itemShortDtoToItemDto(itemShortDto);
        itemDto.setOwner(userMapper.ownerToUser(userService.getUserById(userId)));
        log.info("Item with id = {} has been created", itemDto.getId());
        return itemMapper.itemToItemDto(itemRepository.save(itemMapper.itemDtoToItem(itemDto)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getAllItems(Long userId) {
        Collection<Item> items = itemRepository.findAllByOwnerId(userId);
        log.info("Get all items");
        return getItemList(items);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        ItemDto item = itemMapper.itemToItemDto(itemRepository.findById(itemId).orElseThrow(()
                -> new NotFoundException("Item has not found")));
        item.setComments(getComments(item.getId()));
        setBookings(item, userId);
        log.info("Item with id = {} is uploaded", itemId);
        return item;
    }

    @Override
    public ItemDto updateItemById(ItemShortDto itemShortDto, Long itemId, Long userId) {
        Optional<Item> updatedItem = itemRepository.findById(itemId);
        if (!updatedItem.get().getOwner().getId().equals(userId)) {
            log.info("Item with id = {} doesn't found", itemId);
            throw new NotFoundException("Item with id " + itemId + " doesn't found");
        }
        if (itemShortDto.getName() != null && !itemShortDto.getName().isBlank()) {
            updatedItem.get().setName(itemShortDto.getName());
        }
        if (itemShortDto.getDescription() != null && !itemShortDto.getDescription().isBlank()) {
            updatedItem.get().setDescription(itemShortDto.getDescription());
        }
        if (itemShortDto.getAvailable() != null) {
            updatedItem.get().setAvailable(itemShortDto.getAvailable());
        }
        log.info("Item with id = {} has been updated", itemId);
        return itemMapper.itemToItemDto(itemRepository.save(updatedItem.get()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> searchItemByText(String stringText) {
        String text = stringText.toLowerCase();
        if (text.isBlank()) {
            return List.of();
        }
        Collection<Item> items = itemRepository.findByDescription(text);
        log.info("Get all items with text {}", text);
        return getItemList(items);
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Long userId, Long itemId) {
        Comment comment = Comment
                .builder()
                .text(commentDto.getText())
                .build();
        comment.setAuthor(userMapper.userDtotoUser(userService.getUserById(userId)));

        comment.setItem((itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item  hasn't be found"))));

        comment.setAuthor(userMapper.userDtotoUser(userService.getUserById(userId)));
        if (!bookingRepository.existsByBookerIdAndEndBeforeAndStatus(userId, LocalDateTime.now(), Status.APPROVED)) {
            throw new NotAvailableException("Comment can't be created");
        }
        comment.setCreated(LocalDateTime.now());
        log.info("Comment has been added");
        return commentMapper.commentToCommentDto(commentRepository.save(comment));
    }

    private List<ItemDto> getItemList(Collection<Item> items) {
        List<ItemDto> itemDtoList = items.stream().map(itemMapper::itemToItemDto).collect(Collectors.toList());
        List<Long> idItems = itemDtoList.stream().map(ItemDto::getId).collect(Collectors.toList());
        Map<Long, BookingItemDto> lastBookings = bookingRepository.findFirstByItemIdInAndStartLessThanEqualAndStatus(
                        idItems, LocalDateTime.now(), Status.APPROVED, Sort.by(DESC, "start"))
                .stream()
                .map(bookingMapper::bookingToItemBookingDto)
                .collect(Collectors.toMap(BookingItemDto::getItemId, Function.identity()));
        itemDtoList.forEach(i -> i.setLastBooking(lastBookings.get(i.getId())));
        Map<Long, BookingItemDto> nextBookings = bookingRepository.findFirstByItemIdInAndStartAfterAndStatus(
                        idItems, LocalDateTime.now(), Status.APPROVED, Sort.by(Sort.Direction.ASC, "start"))
                .stream()
                .map(bookingMapper::bookingToItemBookingDto)
                .collect(Collectors.toMap(BookingItemDto::getItemId, Function.identity()));
        itemDtoList.forEach(i -> i.setNextBooking(nextBookings.get(i.getId())));

        Map<Long, List<CommentDto>> comments = commentRepository.findByItemIdIn(idItems, Sort.by(DESC, "created"))
                .stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.groupingBy(CommentDto::getId));

        itemDtoList.forEach(i -> i.setComments(comments.get(i.getId())));
        itemDtoList.sort(comparing(ItemDto::getId));
        return itemDtoList;
    }

    private List<CommentDto> getComments(Long itemId) {
        List<Comment> comments = commentRepository.findByItemId(itemId);
        log.info("Get all comments");
        return comments.stream()
                .map(commentMapper::commentToCommentDto)
                .collect(Collectors.toList());
    }

    private ItemDto setBookings(ItemDto itemDto, Long userId) {
        if (itemDto.getOwner().getId().equals(userId)) {
            itemDto.setLastBooking(
                    bookingRepository.findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(itemDto.getId(),
                                    LocalDateTime.now(), Status.APPROVED).map(bookingMapper::bookingToItemBookingDto)
                            .orElse(null));

            itemDto.setNextBooking(
                    bookingRepository.findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(itemDto.getId(),
                                    LocalDateTime.now(), Status.APPROVED).map(bookingMapper::bookingToItemBookingDto)
                            .orElse(null));

            return itemDto;
        }
        log.info("Booking has been set");
        return itemDto;
    }

}