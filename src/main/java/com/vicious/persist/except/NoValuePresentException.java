package com.vicious.persist.except;

public class NoValuePresentException extends RuntimeException{
    public NoValuePresentException() {
    }

    public NoValuePresentException(String message) {
        super(message);
    }

    public NoValuePresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoValuePresentException(Throwable cause) {
        super(cause);
    }

    public NoValuePresentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
