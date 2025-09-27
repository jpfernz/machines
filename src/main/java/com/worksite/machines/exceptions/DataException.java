package com.worksite.machines.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataException extends RuntimeException {
    public DataException(String message) {
        super(message);
        log.error("DataException occurred: {}", message);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
        log.error("DataException occurred: {}. Cause: {}", message, cause.getMessage());
    }

}
