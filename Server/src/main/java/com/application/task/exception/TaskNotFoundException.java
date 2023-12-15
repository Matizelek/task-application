package com.application.task.exception;

import org.springframework.core.NestedCheckedException;

public class TaskNotFoundException extends NestedCheckedException {
    public TaskNotFoundException(String msg) {
        super(msg);
    }

    public TaskNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
