package org.molsh.common;

public enum ProcessingTaskStatus {
    Created {
        @Override
        public boolean isNext(ProcessingTaskStatus status) {
            return status == InProgress;
        }
    },
    InProgress {
        @Override
        public boolean isNext(ProcessingTaskStatus status) {
            return status == Complete || status == Canceled;
        }
    },
    Canceled {
        @Override
        public boolean isNext(ProcessingTaskStatus status) {
            return false;
        }
    },
    Complete  {
        @Override
        public boolean isNext(ProcessingTaskStatus status) {
            return false;
        }
    };

    public abstract boolean isNext(ProcessingTaskStatus status);
}
