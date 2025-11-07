package org.molsh.exception;

import org.molsh.common.ProcessingTaskStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ProcessingTaskStatusOrderException extends RuntimeException{
    public ProcessingTaskStatusOrderException(ProcessingTaskStatus statusFrom, ProcessingTaskStatus statusTo) {
        super(String.format("Can't change status %s to %s", statusFrom, statusTo));
    }
}
