package com.application.task.exception;

import org.springframework.core.NestedCheckedException;

public class InvalidInputDataException extends NestedCheckedException {
    public InvalidInputDataException(String msg) {
        super(msg);
    }

    public InvalidInputDataException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
