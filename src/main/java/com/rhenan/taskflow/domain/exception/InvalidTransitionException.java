package com.rhenan.taskflow.domain.exception;

import com.rhenan.taskflow.domain.enums.ActivityStatus;

public class InvalidTransitionException extends BusinessRuleException{
    public InvalidTransitionException(ActivityStatus from, ActivityStatus to) {
        super("Mudanca de status invalida: " + from + " para " + to);
    }

    public InvalidTransitionException(String message) {
        super(message);
    }
}
