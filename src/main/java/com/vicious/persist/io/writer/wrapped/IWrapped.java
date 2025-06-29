package com.vicious.persist.io.writer.wrapped;

/**
 * Simple interface denoting that an object wraps another.
 * @author Jack Andersen
 * @since 1.0
 * @param <T> The type being wrapped.
 */
public interface IWrapped<T> {
    /**
     * Unwraps the wrapped object. This can be computed in-method.
     * @return the wrapped object.
     */
    T unwrap();
}
