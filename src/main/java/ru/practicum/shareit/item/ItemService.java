package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.common.CrudService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService extends CrudService<Integer, Item, ItemDto> {
    private final UserRepository userRepository;

    @Autowired
    public ItemService(ItemMapper mapper,
                       ItemRepository repository,
                       UserRepository userRepository) {
        super(mapper, repository);
        this.userRepository = userRepository;
    }

    public List<ItemDto> findAll(Integer userId) {
        return ((ItemRepository) repository).findByUserId(userId)
                .stream()
                .map(mapper::convert)
                .collect(Collectors.toList());
    }

    public List<ItemDto> search(String search) {
        return ((ItemRepository) repository).findBySearchString(search)
                .stream()
                .map(mapper::convert)
                .collect(Collectors.toList());
    }

    public ItemDto save(Integer userId, ItemDto dto) {
        dto.setUserId(userId);
        return super.save(dto);
    }

    public ItemDto patch(Integer userId, Integer dtoId, ItemDto dto) {
        dto.setUserId(userId);
        return super.patch(dtoId, dto);
    }

    @Override
    protected Void validate(Item model) {
        userRepository.findById(model.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Optional<Item> item = repository.findById(model.getId());
        if (item.isPresent()) {
            if (!item.get().getUserId().equals(model.getUserId())) {
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
