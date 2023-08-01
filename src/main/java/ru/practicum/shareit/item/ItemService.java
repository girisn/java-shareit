package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.common.CrudService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService extends CrudService<Long, Item, ItemDto> {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemMapper mapper,
                       ItemRepository repository,
                       UserRepository userRepository) {
        super(mapper, repository);
        this.userRepository = userRepository;
        this.itemRepository = repository;
    }

    public List<ItemDto> findAll(Long userId) {
        return itemRepository.findByUserId(userId)
                .stream()
                .map(mapper::convert)
                .collect(Collectors.toList());
    }

    public List<ItemDto> search(String search) {
        return itemRepository.findByDescriptionContainingIgnoreCase(search)
                .stream()
                .map(mapper::convert)
                .collect(Collectors.toList());
    }

    public ItemDto save(Long userId, ItemDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        dto.setUserId(user.getId());
        return super.save(dto);
    }

    public ItemDto patch(Long userId, Long dtoId, ItemDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        dto.setUserId(user.getId());
        return super.patch(dtoId, dto);
    }

    @Override
    protected Void validate(Item model) {
        userRepository.findById(model.getUser().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Optional<Item> item = itemRepository.findById(model.getId());
        if (item.isPresent()) {
            if (!item.get().getUser().getId().equals(model.getUser().getId())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }

        if (model.getAvailable() == null
                || !StringUtils.hasText(model.getName())
                || !StringUtils.hasText(model.getDescription())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
