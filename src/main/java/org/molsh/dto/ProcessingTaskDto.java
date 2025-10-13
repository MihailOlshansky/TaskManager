package org.molsh.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import org.molsh.common.ProcessingTaskStatus;

import java.time.LocalDateTime;

@Value
@Builder
public class ProcessingTaskDto {
    Long id;
    ProcessingTaskStatus status;
    Integer priority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    LocalDateTime createdDate;
    Long userId;
}
