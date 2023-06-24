package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.CrudRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ItemRepository extends CrudRepository<Integer, Item> {
    public ItemRepository() {
        super(1, (id) -> id + 1);
    }

    public List<Item> findByUserId(Integer userId) {
        return this.storage.values()
                .stream()
                .filter(item -> item.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Item> findBySearchString(String search) {
        if (search.equals("")) {
            return Collections.emptyList();
        }
        return this.storage.values()
                .stream()
                .filter(item -> item.getAvailable().equals(true))
                .filter(item -> item.getDescription().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toList());
    }
}
