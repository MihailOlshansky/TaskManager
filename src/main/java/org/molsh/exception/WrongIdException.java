package org.molsh.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WrongIdException extends RuntimeException {
    public WrongIdException() {
        super("Entity and dto have different ids");
    }
}
