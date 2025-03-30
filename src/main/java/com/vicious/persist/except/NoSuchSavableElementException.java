package com.vicious.persist.except;

/**
 * Indicates that an element marked with {@link com.vicious.persist.annotations.Save} is invalid for some reason.
 * @author Jack Andersen
 * @since 1.0
 */
public class NoSuchSavableElementException extends InvalidAnnotationException {
    public NoSuchSavableElementException() {
    }

    public NoSuchSavableElementException(String message) {
        super(message);
    }

    public NoSuchSavableElementException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchSavableElementException(Throwable cause) {
        super(cause);
    }

    public NoSuchSavableElementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
