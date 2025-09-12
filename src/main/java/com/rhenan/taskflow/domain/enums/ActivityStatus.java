package com.rhenan.taskflow.domain.enums;

import java.util.EnumSet;
import java.util.Set;

public enum ActivityStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED;

    public boolean isFinished() {
        return this == COMPLETED;
    }

    public Set<ActivityStatus> allowedTransition() {
        return switch (this) {
            case PENDING -> EnumSet.of(IN_PROGRESS, COMPLETED);
            case IN_PROGRESS -> EnumSet.of(COMPLETED);
            case COMPLETED -> EnumSet.noneOf(ActivityStatus.class);
        };
    }

    public boolean allowTransition(ActivityStatus value) {
        return allowedTransition().contains(value);
    }
}
