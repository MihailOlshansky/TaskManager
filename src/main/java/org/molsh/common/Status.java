package org.molsh.common;

import java.util.Set;

public enum Status {
    Complete(Set.of()),
    Canceled(Set.of()),
    InProgress(Set.of(Complete, Canceled)),
    Created(Set.of(InProgress));

    private final Set<Status> next;

    Status(Set<Status> next) {
        this.next = next;
    }

    public boolean isNext(Status status) {
        return next.contains(status);
    }
}
