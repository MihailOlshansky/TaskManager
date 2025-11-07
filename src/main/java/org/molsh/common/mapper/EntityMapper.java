package org.molsh.common.mapper;

public interface EntityMapper<E, Dto> {
    Dto entityToDto(E entity);
    void setNotNullProperties(E entity, Dto dto);
}
