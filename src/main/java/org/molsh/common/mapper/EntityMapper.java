package org.molsh.common.mapper;

import org.springframework.stereotype.Component;

@Component
public interface EntityMapper<E, Dto> {
    Dto entityToDto(E entity);
    void setNotNullProperties(E entity, Dto dto);
}
