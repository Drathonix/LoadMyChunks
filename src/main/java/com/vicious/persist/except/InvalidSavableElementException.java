package com.vicious.persist.except;

/**
 * Indicates that an element marked with {@link com.vicious.persist.annotations.Save} is invalid for some reason.
 * @author Jack Andersen
 * @since 1.0
 */
public class InvalidSavableElementException extends InvalidAnnotationException {
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
