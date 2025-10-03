package org.molsh.common;

import java.util.Set;

public enum ProcessingTaskStatus {
    Complete(Set.of()),
    Canceled(Set.of()),
    InProgress(Set.of(Complete, Canceled)),
    Created(Set.of(InProgress));

    private final Set<ProcessingTaskStatus> next;

    ProcessingTaskStatus(Set<ProcessingTaskStatus> next) {
        this.next = next;
    }

    public boolean isNext(ProcessingTaskStatus status) {
        return next.contains(status);
    }
}
