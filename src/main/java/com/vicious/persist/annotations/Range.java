package com.vicious.persist.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Savable elements annotated with this annotation will have their Numerical value automatically clamped when set.
 * @since 1.2.3
 * @author Jack Andersen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Range {
    /**
     * @return the maximum value
     */
    double maximum() default Double.MAX_VALUE;
    /**
     * @return the minimum value
     */
    double minimum() default Double.MIN_VALUE;
}
