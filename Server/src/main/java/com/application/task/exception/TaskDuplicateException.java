package com.application.task.exception;

import org.springframework.core.NestedCheckedException;

public class TaskDuplicateException extends NestedCheckedException {
    public TaskDuplicateException(String msg) {
        super(msg);
    }

    public TaskDuplicateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
