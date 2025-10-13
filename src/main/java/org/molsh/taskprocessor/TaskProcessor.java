package org.molsh.taskprocessor;

import org.molsh.entity.ProcessingTask;

import java.util.Collection;

public interface TaskProcessor {
    void addTask(ProcessingTask task);
    void addTasks(Collection<ProcessingTask> tasks);

    void processTasks();
    void processTask(ProcessingTask task);
}
