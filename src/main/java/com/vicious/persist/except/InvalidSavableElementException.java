package com.vicious.persist.except;

public class InvalidSavableElementException extends RuntimeException {
    public InvalidSavableElementException() {
    }

    public InvalidSavableElementException(String message) {
        super(message);
    }

    public InvalidSavableElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSavableElementException(Throwable cause) {
        super(cause);
    }

    public InvalidSavableElementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
