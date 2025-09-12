package com.rhenan.taskflow.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActivityStatusTest {
    @Test
    void completedEhTerminal() {
        assertTrue(ActivityStatus.COMPLETED.isFinished());
        assertFalse(ActivityStatus.PENDING.isFinished());
        assertFalse(ActivityStatus.IN_PROGRESS.isFinished());
    }

    @Test
    void transicoesValidas() {
        assertTrue(ActivityStatus.PENDING.allowTransition(ActivityStatus.IN_PROGRESS));
        assertTrue(ActivityStatus.PENDING.allowTransition(ActivityStatus.COMPLETED));
        assertTrue(ActivityStatus.IN_PROGRESS.allowTransition(ActivityStatus.COMPLETED));
    }

    @Test
    void transicoesInvalidas() {
        assertFalse(ActivityStatus.IN_PROGRESS.allowTransition(ActivityStatus.PENDING));
        assertFalse(ActivityStatus.COMPLETED.allowTransition(ActivityStatus.IN_PROGRESS));
        assertFalse(ActivityStatus.COMPLETED.allowTransition(ActivityStatus.PENDING));
        assertFalse(ActivityStatus.COMPLETED.allowTransition(ActivityStatus.COMPLETED));
    }
}
