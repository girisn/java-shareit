package ru.practicum.shareit.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CrudService<E, M extends Model<E>, D extends ModelDto<E>> {
    protected DtoMapper<M, D> mapper;
    private JpaRepository<M, E> repository;

    public CrudService(DtoMapper<M, D> mapper, JpaRepository repository) {
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
        this.validate(model);
        model = repository.save(model);
        return mapper.convert(model);
    }

    public D patch(E id, D dto) {
        Optional<M> modelOpt = repository.findById(id);
        if (modelOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        M model = modelOpt.get();

        model = mapper.update(model, dto);
        this.validate(model);
        model = repository.save(model);
        return mapper.convert(model);
    }

    public void remove(E id) {
        repository.deleteById(id);
    }

}
