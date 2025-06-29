package com.vicious.persist.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Grants a field priority in saving, prioritized fields will be listed first in order of highest priority to the lowest priority. Where a value of higher priority is LARGER!
 * This annotation is overridden by {@link Ordering} which is declared at type level.
 * @since 1.4.7
 * @author Jack Andersen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD, ElementType.PARAMETER})
public @interface Priority {
    int value();
}
