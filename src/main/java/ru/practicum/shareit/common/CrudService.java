package ru.practicum.shareit.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CrudService<E, M extends Model<E>, D extends ModelDto<E>> {
    protected DtoMapper<M, D> mapper;
    protected CrudRepository<E, M> repository;

    public CrudService(DtoMapper<M, D> mapper, CrudRepository<E, M> repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    protected abstract Void validate(M model);

    public List<D> findAll() {
        return repository.findAll()
                .stream()
                .map(model -> mapper.convert(model))
                .collect(Collectors.toList());
    }

    public D findById(E id) {
        Optional<M> modelOpt = repository.findById(id);
        if (modelOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return mapper.convert(modelOpt.get());
    }

    public D save(D dto) {
        M model = mapper.convert(dto);
        model = repository.save(model, this::validate);
        return mapper.convert(model);
    }

    public D patch(E id, D dto) {
        Optional<M> modelOpt = repository.findById(id);
        if (modelOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        M model = modelOpt.get();

        model = mapper.update(model, dto);
        model = repository.update(model, this::validate);
        return mapper.convert(model);
    }

    public void remove(E id) {
        repository.remove(id);
    }

}
