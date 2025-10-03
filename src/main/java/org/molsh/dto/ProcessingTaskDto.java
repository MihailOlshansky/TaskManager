package org.molsh.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import org.molsh.common.ProcessingTaskStatus;

import java.util.Date;

@Value
public class ProcessingTaskDto {
    Long id;
    ProcessingTaskStatus status;
    Integer priority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    Date createdDate;
    Long userId;
}
