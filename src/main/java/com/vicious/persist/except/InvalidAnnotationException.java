package com.vicious.persist.except;

public class InvalidAnnotationException extends RuntimeException {
    public InvalidAnnotationException() {
    }

    public InvalidAnnotationException(String message) {
        super(message);
    }

    public InvalidAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAnnotationException(Throwable cause) {
        super(cause);
    }

    public InvalidAnnotationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
