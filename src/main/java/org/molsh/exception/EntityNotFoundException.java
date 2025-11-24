package org.molsh.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String entityName, Long id) {
        super(String.format("%s with id %d not found", entityName, id));
    }

    public EntityNotFoundException(String entityName, String name) {
        super(String.format("%s with name %s not found", entityName, name));
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

}
