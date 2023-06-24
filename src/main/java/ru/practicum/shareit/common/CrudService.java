package ru.practicum.shareit.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CrudService<ID, MODEL extends Model<ID>, DTO extends ModelDto<ID>> {
    protected DtoMapper<MODEL, DTO> mapper;
    protected CrudRepository<ID, MODEL> repository;

    public CrudService(DtoMapper<MODEL, DTO> mapper, CrudRepository<ID, MODEL> repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    protected abstract Void validate(MODEL model);

    public List<DTO> findAll() {
        return repository.findAll()
                .stream()
                .map(model -> mapper.convert(model))
                .collect(Collectors.toList());
    }

    public DTO findById(ID id) {
        Optional<MODEL> modelOpt = repository.findById(id);
        if (modelOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return mapper.convert(modelOpt.get());
    }

    public DTO save(DTO dto) {
        MODEL model = mapper.convert(dto);
        model = repository.save(model, this::validate);
        return mapper.convert(model);
    }

    public DTO patch(ID id, DTO dto) {
        Optional<MODEL> modelOpt = repository.findById(id);
        if (modelOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        MODEL model = modelOpt.get();

        model = mapper.update(model, dto);
        model = repository.update(model, this::validate);
        return mapper.convert(model);
    }

    public void remove(ID id) {
        repository.remove(id);
    }

}
