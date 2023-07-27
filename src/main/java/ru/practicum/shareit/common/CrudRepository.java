package ru.practicum.shareit.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class CrudRepository<E, M extends Model<E>> {
    private E currentId;
    private final Function<E, E> increaseCurrentId;
    protected final Map<E, M> storage = new HashMap<>();

    public CrudRepository(E initialId, Function<E, E> increaseCurrentId) {
        this.currentId = initialId;
        this.increaseCurrentId = increaseCurrentId;
    }

    public Collection<M> findAll() {
        return storage.values();
    }

    public Optional<M> findById(E id) {
        return Optional.ofNullable(storage.get(id));
    }

    public M save(M model, Function<M, Void> validate) {
        validate.apply(model);
        model.setId(currentId);
        storage.put(currentId, model);
        currentId = increaseCurrentId.apply(currentId);
        return model;
    }

    public M update(M model, Function<M, Void> validate) {
        validate.apply(model);
        storage.put(model.getId(), model);
        return model;
    }

    public void remove(E id) {
        storage.remove(id);
    }
}
