package org.molsh.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;

import java.util.Date;

@Value
public class ProcessingTaskDto {
    Long id;
    String status;
    Integer priority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    Date createdDate;
    Long userId;
}
