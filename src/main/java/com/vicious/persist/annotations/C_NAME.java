package com.vicious.persist.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Assigns a unique name to a class type. This is used in replacement of the class canonical name.
 * Intended to be used as a safeguard against refactoring.
 * @since 1.0
 * @author Jack Andersen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface C_NAME {
    String value();
}
