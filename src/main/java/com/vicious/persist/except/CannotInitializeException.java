package com.vicious.persist.except;

public class CannotInitializeException extends RuntimeException {
    public CannotInitializeException() {
    }

    public CannotInitializeException(String message) {
        super(message);
    }

    public CannotInitializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotInitializeException(Throwable cause) {
        super(cause);
    }

    public CannotInitializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
