package ru.practicum.shareit.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class CrudRepository<ID, MODEL extends Model<ID>> {
    private ID currentId;
    private final Function<ID, ID> increaseCurrentId;
    protected final Map<ID, MODEL> storage = new HashMap<>();

    public CrudRepository(ID initialId, Function<ID, ID> increaseCurrentId) {
        this.currentId = initialId;
        this.increaseCurrentId = increaseCurrentId;
    }

    public Collection<MODEL> findAll() {
        return storage.values();
    }

    public Optional<MODEL> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public MODEL save(MODEL model, Function<MODEL, Void> validate) {
        validate.apply(model);
        model.setId(currentId);
        storage.put(currentId, model);
        currentId = increaseCurrentId.apply(currentId);
        return model;
    }

    public MODEL update(MODEL model, Function<MODEL, Void> validate) {
        validate.apply(model);
        storage.put(model.getId(), model);
        return model;
    }

    public void remove(ID id) {
        storage.remove(id);
    }
}
