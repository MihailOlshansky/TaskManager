package org.molsh.service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.molsh.exception.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class EntityService<T, Dto> {
    protected abstract JpaRepository<T, Long> getRepository();

    public abstract String getEntityName();

    @Transactional
    public abstract T create(Dto dto);

    public T find(Long id) {
        return getRepository().findById(id).orElse(null);
    }

    public T findNonNull(Long id) {
        return getRepository().findById(id)
                .orElseThrow(() -> new EntityNotFoundException(getEntityName(), id));
    }

    public List<T> findAll(Iterable<Long> ids) {
        return getRepository().findAllById(ids);
    }
}
